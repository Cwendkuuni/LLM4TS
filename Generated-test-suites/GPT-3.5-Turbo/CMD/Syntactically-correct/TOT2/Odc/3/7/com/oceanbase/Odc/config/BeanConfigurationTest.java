import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(new SensitivePropertyHandler());
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = new DefaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        assertNotNull(client);
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());

        try {
            client.listInstances();
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // success
        }

        try {
            client.listTenants("testInstanceId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // success
        }

        try {
            client.getTenant("testInstanceId", "testTenantId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // success
        }

        try {
            client.listDatabaseUsers("testInstanceId", "testTenantId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // success
        }

        assertNotNull(client.getTenantEndpoint("testInstanceId", "testTenantId"));
        
        try {
            client.getSysTenantUser("testInstanceId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // success
        }
    }
}