package com.oceanbase.odc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    BeanConfiguration beanConfiguration;

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @BeforeEach
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient ncmc = new BeanConfiguration.NullCloudMetadataClient();
        ncmc.init();
        assertFalse(ncmc.supportsCloudMetadata());
        assertFalse(ncmc.supportsTenantInstance());
        assertFalse(ncmc.needsOBTenantName());
        assertFalse(ncmc.needsSysTenantUser());
        assertTrue(ncmc.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, ncmc.oceanbaseAccessMode());
        Exception exception = assertThrows(UnsupportedException.class, ncmc::listInstances);
        assertEquals("CloudMetadata not supported", exception.getMessage());
        exception = assertThrows(UnsupportedException.class, () -> ncmc.listTenants("test"));
        assertEquals("CloudMetadata not supported", exception.getMessage());
        exception = assertThrows(UnsupportedException.class, () -> ncmc.getTenant("test", "test"));
        assertEquals("getTenant not supported in non-could", exception.getMessage());
        exception = assertThrows(UnsupportedException.class, () -> ncmc.listDatabaseUsers("test", "test"));
        assertEquals("CloudMetadata not supported", exception.getMessage());
        assertNotNull(ncmc.getTenantEndpoint("test", "test"));
        exception = assertThrows(UnsupportedException.class, () -> ncmc.getSysTenantUser("test"));
        assertEquals("CloudMetadata not supported", exception.getMessage());
    }
}
