import org.junit.*;
import static org.junit.Assert.*;

public class ValidateTest {
    
    @Test
    public void testIsTrueWithValue() {
        try {
            Validate.isTrue(false, "The value %s is not true", 10);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 10 is not true", e.getMessage());
        }
    }

    @Test
    public void testIsTrueWithExpression() {
        try {
            Validate.isTrue(false);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated expression is false", e.getMessage());
        }
    }

    @Test
    public void testNotNull() {
        try {
            Validate.notNull(null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }
    
    // add tests for other methods...
}