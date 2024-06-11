import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        //Test Exception
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-8, 3));
        //Test Exception
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(8, 3));
        assertEquals(-5, FieldUtils.safeSubtract(2, 7));
        //Test Exception
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(15, FieldUtils.safeMultiply(3, 5));
        assertEquals(-15, FieldUtils.safeMultiply(3, -5));
        //Test Exception
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5));
        //Test Exception
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = null; // Test with null field
        // Test with value within bounds
        FieldUtils.verifyValueBounds(field, 5, 1, 10);
        // Test with value out of bounds
        try {
            FieldUtils.verifyValueBounds(field, 15, 1, 10);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(-5, 10, 15));
        assertEquals(10, FieldUtils.getWrappedValue(25, 10, 15));
        // Test Exception
        try {
            FieldUtils.getWrappedValue(5, 15, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("abc", "abc"));
        assertFalse(FieldUtils.equals("abc", "def"));
        assertFalse(FieldUtils.equals("abc", null));
        assertTrue(FieldUtils.equals(null, null));
    }
}