import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerMock;

import static org.mockito.Mockito.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        Assert.assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        String jsonString = "{\"name\": \"John Doe\", \"age\": 30}";
        Object javaObject = objectMapper.readValue(jsonString, Object.class);
        Assert.assertNotNull(javaObject);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        DefaultHttpFirewall expectedDefaultHttpFirewall = new DefaultHttpFirewall();
        expectedDefaultHttpFirewall.setAllowUrlEncodedSlash(true);
        Assert.assertEquals(expectedDefaultHttpFirewall, defaultHttpFirewall);
    }

    @Test(expected = org.springframework.beans.factory.UnsatisfiedDependencyException.class)
    public void testCloudMetadataClientMissingBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        Assert.assertThrows(UnsatisfiedDependencyException.class, () -> beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        Assert.assertEquals("JSESSIONID", cookieSerializer.getName());
    }

    @Test
    public void testPasswordEncoderEmptyPassword() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String emptyPassword = "";
        String encodedEmptyPassword = passwordEncoder.encode(emptyPassword);
        Assert.assertEquals(emptyPassword, passwordEncoder.decode(encodedEmptyPassword));
    }

    @Test
    public void testObjectMapperNullInput() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(Mockito.mock(SensitivePropertyHandler.class));
        Assert.assertNull(objectMapper.readValue(null, Object.class));
    }

    @Test
    public void testDefaultHttpFirewallMalformedURL() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        String malformedURL = "http://example.com/<SCRIPT>";
        Assert.assertFalse(defaultHttpFirewall.isSanitized(malformedURL));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCloudMetadataClientNullInstanceId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        Assert.assertThrows(UnsupportedOperationException.class, () -> cloudMetadataClient.listInstances(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCloudMetadataClientNullTenantId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        Assert.assertThrows(UnsupportedOperationException.class, () -> cloudMetadataClient.getTenant(Mockito.mock(String.class), null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCloudMetadataClientUnsupportedInstanceId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        Assert.assertThrows(UnsupportedOperationException.class, () -> cloudMetadataClient.listInstances("unsupportedInstanceId"));
    }

    @Test(expected = NullPointerException.class)
    public void testCookieSerializerNullCookieName() {
        CookieSerializer cookieSerializer = Mockito.mock(CookieSerializer.class);
        Assert.assertThrows(NullPointerException.class, () -> ((DefaultCookieSerializer) cookieSerializer).setCookieName(null));
    }
}