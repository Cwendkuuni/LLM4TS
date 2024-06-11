
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    // Add more test cases for each public method according to step 2

    @Test(expected = UnsupportedException.class)
    public void listInstancesTestException() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }
    // Add more test cases for edge cases according to step 4
}
