import static org.junit.Assert.*;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Typical use case
        Validate.isTrue(true);
        // Edge case
        try {
            Validate.isTrue(false, "Error message");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testNotNull() {
        // Typical use case
        assertNotNull(Validate.notNull(new Object()));
        // Edge case
        try {
            Validate.notNull(null, "Error message");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    // More test methods for other public methods in the Validate class

}