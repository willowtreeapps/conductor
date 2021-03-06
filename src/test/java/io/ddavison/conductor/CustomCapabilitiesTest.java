package io.ddavison.conductor;

import io.ddavison.conductor.util.DriverUtil;
import org.apache.commons.lang3.mutable.Mutable;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CustomCapabilitiesTest {

    private String envCurrentSchemes, envBaseUrl;

    @BeforeMethod(alwaysRun = true)
    public void before() {
        envCurrentSchemes = System.getProperty(ConductorConfig.CONDUCTOR_CURRENT_SCHEMES);
        envBaseUrl = System.getProperty(ConductorConfig.CONDUCTOR_BASE_URL);

        System.clearProperty(ConductorConfig.CONDUCTOR_CURRENT_SCHEMES);
        System.clearProperty(ConductorConfig.CONDUCTOR_BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (envCurrentSchemes != null) {
            System.setProperty(ConductorConfig.CONDUCTOR_CURRENT_SCHEMES, envCurrentSchemes);
        }
        if (envBaseUrl != null) {
            System.setProperty(ConductorConfig.CONDUCTOR_BASE_URL, envBaseUrl);
        }
    }

    @Test(groups = {"modifies-env-vars"})
    public void custom_capabilities_are_being_added() {
        ConductorConfig config = new ConductorConfig("/test_yaml/simple_defaults.yaml");
        DesiredCapabilities caps = new DesiredCapabilities();

        MutableCapabilities capabilities = DriverUtil.buildCustomCapabilities(config, caps);

        Map<String, String> expectedCapabilities = new HashMap<>();
        expectedCapabilities.put("fizz", "buzz");
        expectedCapabilities.put("foo", "bar");

        Assertions.assertThat(capabilities.asMap())
                .containsAllEntriesOf(expectedCapabilities);
    }

    @Test(groups = {"modifies-env-vars"})
    public void current_scheme_custom_capabilities_are_being_added() {
        ConductorConfig config = new ConductorConfig("/test_yaml/all.yaml");
        DesiredCapabilities caps = new DesiredCapabilities();

        MutableCapabilities capabilities =  DriverUtil.buildCustomCapabilities(config, caps);

        Map<String, String> expectedCapabilities = new HashMap<>();
        expectedCapabilities.put("foo", "bar");
        expectedCapabilities.put("fizz", "buzz");
        expectedCapabilities.put("bar", "foo");

        Assertions.assertThat(capabilities.asMap())
                .containsAllEntriesOf(expectedCapabilities);
    }
}
