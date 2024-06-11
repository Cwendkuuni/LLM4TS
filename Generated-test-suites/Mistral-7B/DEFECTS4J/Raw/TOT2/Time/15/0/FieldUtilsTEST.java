import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test with valid integers
        int validInt = 5;
        int expected = -validInt;
        int result = FieldUtils.safeNegate(validInt);
        assertEquals(expected, result);

        // Test with Integer.MIN_VALUE
        int minInt = Integer.MIN_VALUE;
        try {
            int result = FieldUtils.safeNegate(minInt);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}