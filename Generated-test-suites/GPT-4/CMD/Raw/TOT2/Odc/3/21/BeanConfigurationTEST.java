import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;
    
    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
        assertEquals(false, client.supportsCloudMetadata());
    }
    
    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
        assertEquals(false, client.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientincludeClusterNameForJdbcUsername() {
        BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
        assertEquals(true, client.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
    }
}