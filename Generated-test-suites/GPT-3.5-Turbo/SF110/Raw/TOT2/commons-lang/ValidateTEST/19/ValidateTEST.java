import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueLong() {
        try {
            Validate.isTrue(true, "Validation failed", 5L);
        } catch (IllegalArgumentException e) {
            fail("Validation failed for true expression");
        }

        try {
            Validate.isTrue(false, "Validation failed", 5L);
        } catch (IllegalArgumentException e) {
            assertEquals("Validation failed", e.getMessage());
        }
    }

    // include other test cases for other methods

}