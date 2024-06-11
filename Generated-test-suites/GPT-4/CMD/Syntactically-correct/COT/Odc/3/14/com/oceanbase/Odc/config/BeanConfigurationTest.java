package com.oceanbase.odc.config;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;

public class BeanConfigurationTest{

    BeanConfiguration config = new BeanConfiguration();

    @Test
    public void passwordEncoderTest(){
        assertNotNull(config.passwordEncoder());
    }

    @Test
    public void objectMapperTest(){
        SensitivePropertyHandler handler = new SensitivePropertyHandler();
        assertNotNull(config.objectMapper(handler));
    }

    @Test
    public void defaultHttpFirewallTest(){
        assertNotNull(config.defaultHttpFirewall());
    }

    @Test
    public void cloudMetadataClientTest(){
        assertTrue(config.cloudMetadataClient() instanceof NullCloudMetadataClient);
    }

    @Test
    public void cookieSerializerTest(){
        assertNotNull(config.cookieSerializer());
    }

    @Test
    public void nullCloudMetadataClientTest(){
        NullCloudMetadataClient client = config.new NullCloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(client.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
        assertThrows(UnsupportedException.class, client::listInstances);
        assertThrows(UnsupportedException.class, () -> client.listTenants("id"));
        assertThrows(UnsupportedException.class, () -> client.getTenant("id", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("id", "tenantId"));
        assertNotNull(client.getTenantEndpoint("id", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("id"));
    }
}