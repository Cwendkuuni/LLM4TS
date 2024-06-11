package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(new MockSensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertNotNull(client);
        assertTrue(client instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        
        assertEquals(false, client.supportsCloudMetadata());
        assertEquals(false, client.supportsTenantInstance());
        assertEquals(false, client.needsOBTenantName());
        assertEquals(false, client.needsSysTenantUser());
        assertEquals(true, client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> client.listInstances());
        assertThrows(UnsupportedException.class, () -> client.listTenants(null));
        assertThrows(UnsupportedException.class, () -> client.getTenant(null, null));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers(null, null));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser(null));
    }
}

class MockSensitivePropertyHandler extends SensitivePropertyHandler {
    // Mock your SensitivePropertyHandler behaviour here
}