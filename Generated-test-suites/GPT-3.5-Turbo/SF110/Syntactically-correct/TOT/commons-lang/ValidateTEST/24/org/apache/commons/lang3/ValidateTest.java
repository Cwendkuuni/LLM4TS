import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    // Test cases for isTrue() method

    @Test
    public void testIsTrueWithTrueExpression() {
        // Typical use case
        assertTrue(Validate.isTrue(true));
    }
    
    @Test
    public void testIsTrueWithFalseExpression() {
        // Typical use case
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated expression is false", e.getMessage());
        }
    }
    
    // Test cases for notNull() method

    @Test
    public void testNotNullWithNonNullObject() {
        // Typical use case
        assertNotNull(Validate.notNull("Hello"));
    }
    
    @Test
    public void testNotNullWithNullObject() {
        // Error scenario
        try {
            Validate.notNull(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }

    // Add more test cases for other methods
    
}