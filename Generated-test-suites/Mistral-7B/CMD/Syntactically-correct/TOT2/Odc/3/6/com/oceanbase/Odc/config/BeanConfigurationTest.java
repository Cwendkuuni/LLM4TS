import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.config.BeanConfiguration;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        String originalPassword = "password123";
        String encodedPassword = passwordEncoder.encode("password123");
        String decodedPassword = passwordEncoder.decode("password123");

        assertEquals(encodedPassword, passwordEncoder.encode(originalPassword));
        assertEquals(originalPassword, decodedPassword);
    }
}