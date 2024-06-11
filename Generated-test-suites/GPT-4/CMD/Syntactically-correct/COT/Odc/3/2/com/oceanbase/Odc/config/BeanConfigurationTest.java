package com.oceanbase.odc.config;
import org.junit.Test;
import com.oceanbase.odc.config.BeanConfiguration;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration= new BeanConfiguration();
    
    @Test
    public void passwordEncoderTest() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }
    
    @Test
    public void objectMapperTest() {
        // Provide a mock SensitivePropertyHandler as input
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }
    
    @Test
    public void defaultHttpFirewallTest() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }
    
    @Test
    public void cloudMetadataClientTest() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }
    
    @Test
    public void cookieSerializerTest() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }    
}