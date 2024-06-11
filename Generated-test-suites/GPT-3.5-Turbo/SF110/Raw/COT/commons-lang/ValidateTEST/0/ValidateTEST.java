
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndLong() {
        try {
            Validate.isTrue(false, "The value %d is false", 10L);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 10 is false", e.getMessage());
        }
    }

    @Test
    public void testIsTrueWithMessageAndDouble() {
        try {
            Validate.isTrue(false, "The value %.2f is false", 3.14);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 3.14 is false", e.getMessage());
        }
    }

    @Test
    public void testIsTrueWithMessageAndValues() {
        try {
            Validate.isTrue(false, "Values: %s, %s", "Hello", "World");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Values: Hello, World", e.getMessage());
        }
    }

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        // No exception should be thrown
    }

    @Test
    public void testNotNull() {
        String str = Validate.notNull("Hello");
        assertEquals("Hello", str);
    }

    @Test
    public void testNotNullWithMessage() {
        try {
            String str = Validate.notNull(null, "The value is null");
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("The value is null", e.getMessage());
        }
    }

    // More test cases for other public methods in the Validate class...

}
