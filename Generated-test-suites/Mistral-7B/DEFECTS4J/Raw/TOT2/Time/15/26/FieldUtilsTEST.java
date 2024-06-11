import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test typical usage
        int input = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(input);
        assertEquals(expected, result);

        // Test edge cases
        int minValue = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(minValue);
            fail("Expected ArithmeticException when negating MIN_VALUE");
        } catch (ArithmeticException e) {
            // Expected
        }

        int maxValue = Integer.MAX_VALUE;
        int result2 = FieldUtils.safeNegate(maxValue);
        assertEquals(-maxValue, result2);
    }
}