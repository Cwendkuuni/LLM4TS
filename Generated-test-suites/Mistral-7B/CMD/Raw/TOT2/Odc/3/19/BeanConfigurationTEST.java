import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String plainTextPassword = "password";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertTrue(passwordEncoder.matches(plainTextPassword, encodedPassword));
    }

    @Test
    public void testEmptyPassword() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String plainTextPassword = "";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertFalse(passwordEncoder.matches(plainTextPassword, encodedPassword));
    }

    @Test
    public void testShortPassword() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String plainTextPassword = "short";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertFalse(passwordEncoder.matches(plainTextPassword, encodedPassword));
    }

    @Test
    public void testPasswordWithSpecialCharacters() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String plainTextPassword = "password@123";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertTrue(passwordEncoder.matches(plainTextPassword, encodedPassword));
    }

    @Test(expected = NullPointerException.class)
    public void testEmptyPasswordEncoder() {
        String plainTextPassword = "password";
        PasswordEncoder passwordEncoder = null;
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertFalse(passwordEncoder.matches(plainTextPassword, encodedPassword));
    }

    @Test(expected = NullPointerException.class)
    public void testNullPasswordEncoder() {
        String plainTextPassword = "password";
        PasswordEncoder passwordEncoder = null;
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertFalse(passwordEncoder.matches(plainTextPassword, encodedPassword));
    }
}