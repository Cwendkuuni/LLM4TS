import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
@ActiveProfiles("test")
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpFirewall defaultHttpFirewall;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Autowired
    private CookieSerializer cookieSerializer;

    @InjectMocks
    @Autowired
    private NullCloudMetadataClient nullCloudMetadataClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        String plainTextPassword = "password";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertNotNull(encodedPassword);
        boolean decoded = passwordEncoder.matches(plainTextPassword, encodedPassword);
        assertTrue(decoded);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = this.beanConfiguration.objectMapper(null);
        assertNotNull(objectMapper);
        CustomOutputSerializer customOutputSerializer = objectMapper.getSerializerProvider().getSerializerProvider().getSerializerConfig().findSerializerValuesForAnnotation(Object.class, Internationalizable.class);
        assertTrue(customOutputSerializer instanceof I18nOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertNotNull(nullCloudMetadataClient);
        when(nullCloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(nullCloudMetadataClient.supportsTenantInstance()).thenReturn(false);
        when(nullCloudMetadataClient.needsOBTenantName()).thenReturn(false);
        when(nullCloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(nullCloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
        when(nullCloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
        when(nullCloudMetadataClient.listInstances()).thenReturn(null);
        when(nullCloudMetadataClient.listTenants(null)).thenReturn(null);
        when(nullCloudMetadataClient.getTenant(null, null)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        when(nullCloudMetadataClient.listDatabaseUsers(null, null)).thenReturn(null);
        when(nullCloudMetadataClient.getTenantEndpoint(null, null)).thenReturn(new OBTenantEndpoint());
        when(nullCloudMetadataClient.getSysTenantUser(null)).thenThrow(new UnsupportedException("CloudMetadata not supported"));

        assertNotNull(beanConfiguration.cloudMetadataClient());
        assertEquals(nullCloudMetadataClient, beanConfiguration.cloudMetadataClient());
    }
}