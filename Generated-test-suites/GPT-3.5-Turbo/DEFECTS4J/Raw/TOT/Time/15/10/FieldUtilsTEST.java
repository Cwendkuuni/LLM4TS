import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(3, 2));
        assertEquals(-8, FieldUtils.safeAdd(-3, -5));
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE - 1, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE + 1, -1));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: " + Integer.MAX_VALUE + " + " + 1, e.getMessage());
        }
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -1);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: " + Integer.MIN_VALUE + " + " + -1, e.getMessage());
        }
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(-15, FieldUtils.safeMultiply(3, -5));
        assertEquals(0, FieldUtils.safeMultiply(0, 5));
        assertEquals(24, FieldUtils.safeMultiply(4, 6));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: " + Integer.MAX_VALUE + " * " + 2, e.getMessage());
        }
        try {
            FieldUtils.safeMultiply(Integer.MIN_VALUE, 2);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: " + Integer.MIN_VALUE + " * " + 2, e.getMessage());
        }
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(10, FieldUtils.safeToInt(10));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Value cannot fit in an int: " + Long.MAX_VALUE, e.getMessage());
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(5, 0, 10));
        assertEquals(10, FieldUtils.getWrappedValue(5, 10, 15));
        assertEquals(15, FieldUtils.getWrappedValue(5, 15, 20));
        assertEquals(5, FieldUtils.getWrappedValue(15, 0, 10));
        assertEquals(10, FieldUtils.getWrappedValue(15, 10, 15));
        assertEquals(15, FieldUtils.getWrappedValue(15, 15, 20));
        try {
            FieldUtils.getWrappedValue(5, 10, 5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("MIN > MAX", e.getMessage());
        }
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertTrue(FieldUtils.equals("abc", "abc"));
        assertFalse(FieldUtils.equals(null, "abc"));
        assertFalse(FieldUtils.equals("abc", null));
        assertFalse(FieldUtils.equals("abc", "def"));
    }
    
}