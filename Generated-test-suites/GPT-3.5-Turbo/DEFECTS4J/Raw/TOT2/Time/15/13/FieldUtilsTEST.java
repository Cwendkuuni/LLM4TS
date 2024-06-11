import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(-10, FieldUtils.safeAdd(-5, -5));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    // Add more test cases for safeAdd, safeSubtract, safeMultiply, safeToInt, safeMultiplyToInt 

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = null; // Initialize with actual field
        try {
            FieldUtils.verifyValueBounds(field, 10, 1, 5);
            fail("IllegalFieldValueException should be thrown");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    // Add more test cases for verifyValueBounds, getWrappedValue, and equals methods
    
}