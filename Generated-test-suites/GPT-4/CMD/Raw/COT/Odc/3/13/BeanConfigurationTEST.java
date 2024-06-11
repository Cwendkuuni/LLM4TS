import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();

        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    // Add further JUnit tests for the remaining methods here...

}