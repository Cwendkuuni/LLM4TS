package com.oceanbase.odc.config;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {
    private BeanConfiguration configUnderTest;
    
    @Before
    public void setup() {
        configUnderTest = new BeanConfiguration();
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = configUnderTest.defaultHttpFirewall();
        assertTrue(firewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) firewall).isAllowUrlEncodedSlash());
    }

    /*
    Add other test methods here...
    */
}