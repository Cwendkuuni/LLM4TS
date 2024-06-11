
package com.oceanbase.odc.config;

import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpFirewall httpFirewall;

    @Autowired
    private BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient;


    @Test
    public void testPasswordEncoder() {
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testHttpFirewall() {
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }
    
    // You should add exception tests for the rest of the methods in NullCloudMetadataClient 
    // which have "UnsupportedException" on this way
    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        nullCloudMetadataClient.listInstances();
    }
}
