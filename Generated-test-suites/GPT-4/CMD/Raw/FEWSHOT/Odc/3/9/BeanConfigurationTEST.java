package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.security.web.firewall.HttpFirewall;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {
    
    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void passwordEncoderTest() {
        BeanConfiguration config = new BeanConfiguration();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals("BCryptPasswordEncoder", passwordEncoder.getClass().getSimpleName());
    }

    @Test
    public void objectMapperTest() {
        BeanConfiguration config = new BeanConfiguration();
        ObjectMapper objectMapper = config.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void defaultHttpFirewallTest() {
        BeanConfiguration config = new BeanConfiguration();
        HttpFirewall defaultHttpFirewall = config.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void cloudMetadataClientTest() {
        BeanConfiguration config = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = config.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void cookieSerializerTest() {
        BeanConfiguration config = new BeanConfiguration();
        CookieSerializer cookieSerializer = config.cookieSerializer();
        assertNotNull(cookieSerializer);
    }
    
    @Test
    public void nullCloudMetadataClientTest() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration().new NullCloudMetadataClient();
        assertNotNull(nullClient);
        assertFalse(nullClient.supportsCloudMetadata());
        assertFalse(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());
    }

}