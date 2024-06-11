package com.oceanbase.odc.config;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfig;
    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        beanConfig = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = beanConfig.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfig.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfig.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        NullCloudMetadataClient client = beanConfig.new NullCloudMetadataClient();

        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(client.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);

        assertThrows(UnsupportedException.class, () -> client.listInstances());
        assertThrows(UnsupportedException.class, () -> client.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> client.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("instanceId"));

        assertNotNull(client.getTenantEndpoint("instanceId", "tenantId"));
    }
}