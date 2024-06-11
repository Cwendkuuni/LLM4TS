import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}