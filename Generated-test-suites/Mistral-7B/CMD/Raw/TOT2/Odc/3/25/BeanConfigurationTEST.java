import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String plainTextPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        boolean decodedPasswordEqualsPlainText = passwordEncoder.matches(encodedPassword, plainTextPassword);
        assertTrue(decodedPasswordEqualsPlainText);
    }

    // Add test cases for other methods here
}