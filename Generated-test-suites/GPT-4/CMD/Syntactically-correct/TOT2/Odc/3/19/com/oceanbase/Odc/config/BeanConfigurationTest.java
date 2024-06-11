package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
       
    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }
    
    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        String encodedPw = passwordEncoder.encode("TestPassword123");
        assertTrue(passwordEncoder.matches("TestPassword123", encodedPw));
        assertFalse(passwordEncoder.matches("InvalidPassword456", encodedPw));
    }
    
    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }
}