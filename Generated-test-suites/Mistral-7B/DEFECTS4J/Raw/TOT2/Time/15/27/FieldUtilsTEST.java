import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Testing normal scenarios
        int input = 5;
        int result = FieldUtils.safeNegate(input);
        assertEquals(-5, result);

        // Testing edge cases
        input = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(input);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Testing error scenarios
        input = -5;
        try {
            result = FieldUtils.safeNegate(input);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}