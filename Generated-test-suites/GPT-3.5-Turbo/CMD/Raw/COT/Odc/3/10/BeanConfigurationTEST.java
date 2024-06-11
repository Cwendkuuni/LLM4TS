
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import javax.validation.constraints.NotBlank;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import javax.annotation.PostConstruct;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {
    
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = cookieSerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullClient = new NullCloudMetadataClient();
        assertNotNull(nullClient);
    }
    
    @Test
    public void testNullCloudMetadataClientMethods() {
        NullCloudMetadataClient nullClient = new NullCloudMetadataClient();
        assertNotNull(nullClient.supportsCloudMetadata());
        assertNotNull(nullClient.supportsTenantInstance());
        assertNotNull(nullClient.needsOBTenantName());
        assertNotNull(nullClient.needsSysTenantUser());
        assertNotNull(nullClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());
    }  
}
