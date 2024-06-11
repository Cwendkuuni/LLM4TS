import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}