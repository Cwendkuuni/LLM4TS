import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(originalPassword, decodedPassword);
    }
}