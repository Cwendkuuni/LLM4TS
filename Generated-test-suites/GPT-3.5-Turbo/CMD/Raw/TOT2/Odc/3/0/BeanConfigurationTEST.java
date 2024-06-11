import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.validation.constraints.NotBlank;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler handler = Mockito.mock(SensitivePropertyHandler.class);
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.objectMapper(handler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        NullCloudMetadataClient cloudClient = beanConfig.cloudMetadataClient();
        assertNotNull(cloudClient);
        assertEquals(false, cloudClient.supportsCloudMetadata());
        assertEquals(false, cloudClient.supportsTenantInstance());
        assertEquals(false, cloudClient.needsOBTenantName());
        assertEquals(false, cloudClient.needsSysTenantUser());
        assertEquals(true, cloudClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudClient.oceanbaseAccessMode());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        CookieSerializer cookieSerializer = beanConfig.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient cloudClient = new NullCloudMetadataClient();
        try {
            cloudClient.listInstances();
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            assertTrue(e.getMessage().contains("CloudMetadata not supported"));
        }
        
        //test other methods in NullCloudMetadataClient 
        //based on similar approach
    }

    // Add more test cases as needed for other methods in BeanConfiguration class

}