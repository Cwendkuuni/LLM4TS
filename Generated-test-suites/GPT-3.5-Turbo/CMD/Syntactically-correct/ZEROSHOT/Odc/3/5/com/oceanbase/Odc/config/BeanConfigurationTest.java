import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoderBean() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapperBean() {
        assertNotNull(beanConfiguration.objectMapper(null));
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClientBean() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializerBean() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        assertFalse(beanConfiguration.new NullCloudMetadataClient().supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        assertFalse(beanConfiguration.new NullCloudMetadataClient().supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        assertFalse(beanConfiguration.new NullCloudMetadataClient().needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        assertFalse(beanConfiguration.new NullCloudMetadataClient().needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        assertTrue(beanConfiguration.new NullCloudMetadataClient().includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        assertEquals(beanConfiguration.new NullCloudMetadataClient().oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
    }

    // Add more test methods for other beans and methods in BeanConfiguration class

}