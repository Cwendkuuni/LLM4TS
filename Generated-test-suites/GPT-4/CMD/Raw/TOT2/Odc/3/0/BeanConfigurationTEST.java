import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;

public class BeanConfigurationTest {

    @Test
    public void passwordEncoderTest() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull("Test failed: Expected non-null instance of BCryptPasswordEncoder", beanConfiguration.passwordEncoder());
        assertTrue("Test failed: Expected instance of BCryptPasswordEncoder", beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void nullCloudMetadataClient_supportsCloudMetadata_Test() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        assertFalse("Test failed: Expected supportsCloudMetadata return false", nullCloudMetadataClient.supportsCloudMetadata());
    }
    
    //Add more tests for each public method
}