import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(5, FieldUtils.safeNegate(-5));
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(-5, FieldUtils.safeAdd(0, -5));
        assertEquals(0, FieldUtils.safeAdd(5, -5));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertEquals(-5, FieldUtils.safeSubtract(0, 5));
        assertEquals(0, FieldUtils.safeSubtract(5, 5));
        try {
            FieldUtils.safeSubtract(Integer.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(15, FieldUtils.safeMultiply(5, 3));
        assertEquals(-15, FieldUtils.safeMultiply(5, -3));
        assertEquals(0, FieldUtils.safeMultiply(5, 0));
        assertEquals(1, FieldUtils.safeMultiply(1, 1));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testSafeMultiplyLong() {
        assertEquals(15, FieldUtils.safeMultiply(5L, 3));
        assertEquals(-15, FieldUtils.safeMultiply(5L, -3));
        assertEquals(0, FieldUtils.safeMultiply(5L, 0));
        assertEquals(1, FieldUtils.safeMultiply(1L, 1));
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(15, FieldUtils.safeMultiply(5L, 3L));
        assertEquals(-15, FieldUtils.safeMultiply(5L, -3L));
        assertEquals(0, FieldUtils.safeMultiply(5L, 0L));
        assertEquals(1, FieldUtils.safeMultiply(1L, 1L));
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5L));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(15, FieldUtils.safeMultiplyToInt(5L, 3L));
        assertEquals(-15, FieldUtils.safeMultiplyToInt(5L, -3L));
        assertEquals(0, FieldUtils.safeMultiplyToInt(5L, 0L));
        assertEquals(1, FieldUtils.safeMultiplyToInt(1L, 1L));
        try {
            FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = null; // TODO: create field object
        try {
            FieldUtils.verifyValueBounds(field, 10, 0, 5);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }
    
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = null; // TODO: create fieldType object
        try {
            FieldUtils.verifyValueBounds(fieldType, 10, 0, 5);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }
    
    @Test
    public void testVerifyValueBoundsString() {
        try {
            FieldUtils.verifyValueBounds("Field", 10, 0, 5);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(8, 5, 10));
        assertEquals(9, FieldUtils.getWrappedValue(1, -10, 10));
        try {
            FieldUtils.getWrappedValue(1, 10, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("abc", "abc"));
        assertFalse(FieldUtils.equals("abc", "def"));
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, "abc"));
        assertFalse(FieldUtils.equals("abc", null));
    }
}