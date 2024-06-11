package com.oceanbase.odc.config;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = new SensitivePropertyHandler();
    }

    @Test
    public void test_A_PasswordEncoder_Present() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull("passwordEncoder is present", passwordEncoder);
    }

    @Test
    public void test_B_ObjectMapper_Present() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull("ObjectMapper is present", objectMapper);
    }

    @Test
    public void test_C_HttpFirewall_Correct_Mode() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull("HttpFirewall is present", httpFirewall);
        assertTrue("HttpFirewall mode set to allowUrlEncodedSlash", httpFirewall.getFirewallConfig().isAllowUrlEncodedSlash());
    }

    // Additional test cases to add depending on business requirement
    // Test presence of CloudMetadataClient
    // Test attributes of NullCloudMetadataClient
    // Test execution of non-implemented methods in NullCloudMetadataClient
    // Test the CookieSerializer properties
}