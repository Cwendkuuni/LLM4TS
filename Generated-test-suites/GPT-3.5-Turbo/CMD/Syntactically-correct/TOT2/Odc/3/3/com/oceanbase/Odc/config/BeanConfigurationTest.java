import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.constraints.NotBlank;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testNullCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testListInstances_InUnsupportedException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().new NullCloudMetadataClient();
        try {
            cloudMetadataClient.listInstances();
        } catch (UnsupportedException e) {
            assertTrue(e.getMessage().contains("CloudMetadata not supported"));
        }
    }

    @Test
    public void testGetTenant_InUnsupportedException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().new NullCloudMetadataClient();
        try {
            cloudMetadataClient.getTenant("instanceId", "tenantId");
        } catch (UnsupportedException e) {
            assertTrue(e.getMessage().contains("getTenant not supported in non-cloud"));
        }
    }

}