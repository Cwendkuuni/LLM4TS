import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Typical use case
        Validate.isTrue(true);

        // Edge case
        try {
            Validate.isTrue(false, "Value %d is not true", 10);
        } catch (IllegalArgumentException e) {
            // Exception handling scenario
            return;
        }
        fail("Expected IllegalArgumentException was not thrown");
    }
    
    @Test
    public void testNotNull() {
        // Typical use case
        String str = "Test";
        assertNotNull(Validate.notNull(str));

        // Edge case
        try {
            Validate.notNull(null, "Object cannot be null");
        } catch (NullPointerException e) {
            // Exception handling scenario
            return;
        }
        fail("Expected NullPointerException was not thrown");
    }

    // Add more test cases for other methods...

}