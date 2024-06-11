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
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for safeAdd as edge case
    @Test
    public void testSafeAddWithEdgeCases() {
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(10, FieldUtils.safeMultiply(2, 5));
        assertEquals(-10, FieldUtils.safeMultiply(2, -5));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for safeMultiply as edge case
    @Test
    public void testSafeMultiplyWithEdgeCases() {
        assertEquals(0, FieldUtils.safeMultiply(Integer.MAX_VALUE, 0));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(1, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(-1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for safeToInt as edge case
    @Test
    public void testSafeToIntWithEdgeCases() {
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Long.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 2000, 1000, 1500);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
        FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 1200, 1000, 1500);
    }

    // Additional test cases for verifyValueBounds as edge case
    @Test
    public void testVerifyValueBoundsWithEdgeCases() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 1000, 1000, 1500);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
        FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 1500, 1000, 1500);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(10, 1, 5));
        assertEquals(10, FieldUtils.getWrappedValue(0, 5, 10));
        assertEquals(5, FieldUtils.getWrappedValue(0, 5, 10));
        try {
            FieldUtils.getWrappedValue(10, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Additional test cases for getWrappedValue as edge case
    @Test
    public void testGetWrappedValueWithEdgeCases() {
        assertEquals(5, FieldUtils.getWrappedValue(10, -5, 10));
        assertEquals(10, FieldUtils.getWrappedValue(10, 1, 10));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("abc", "abc"));
        assertFalse(FieldUtils.equals("abc", "def"));
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals("abc", null));
    }

}