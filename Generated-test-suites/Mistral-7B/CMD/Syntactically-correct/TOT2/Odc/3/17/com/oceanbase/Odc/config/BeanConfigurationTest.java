import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        String plainText = "password123";
        String encodedPassword = passwordEncoder.encode(plainText);
        assertNotNull(encodedPassword);
        boolean isMatch = passwordEncoder.matches(plainText, encodedPassword);
        assertTrue(isMatch);
    }

}