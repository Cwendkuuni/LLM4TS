import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.config.BeanConfiguration;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String originalPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(originalPassword, decodedPassword);
    }
}