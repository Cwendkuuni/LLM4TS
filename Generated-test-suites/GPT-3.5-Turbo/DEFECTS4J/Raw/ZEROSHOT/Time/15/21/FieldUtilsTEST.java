import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.*;

public class FieldUtilsTest {
      
    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeNegateWithMinValue() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(8, FieldUtils.safeAdd(5, 3));
        assertEquals(-4, FieldUtils.safeAdd(-5, 1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeAddWithOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }
    
    @Test
    public void testSafeAddWithZero() {
        assertEquals(-5, FieldUtils.safeAdd(-5, 0));
        assertEquals(5, FieldUtils.safeAdd(0, 5));
    }
    
    @Test
    public void testSafeSubtract() {
        assertEquals(2, FieldUtils.safeSubtract(5, 3));
        assertEquals(-6, FieldUtils.safeSubtract(-5, 1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractWithOverflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(15, FieldUtils.safeMultiply(5, 3));
        assertEquals(4, FieldUtils.safeMultiply(-2, -2));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeMultiplyToLong() {
        assertEquals(15L, FieldUtils.safeMultiply(5L, 3L));
        assertEquals(4L, FieldUtils.safeMultiply(-2L, -2L));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToLongWithOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5L));
        assertEquals(-5, FieldUtils.safeToInt(-5L));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeToIntWithOverflow() {
        FieldUtils.safeToInt(10000000000L);
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(15, FieldUtils.safeMultiplyToInt(5L, 3L));
        assertEquals(4, FieldUtils.safeMultiplyToInt(-2L, -2L));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntWithOverflow() {
        FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L);
    }
    
    @Test
    public void testVerifyValueBoundsWithValidValue() {
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfDay();
        FieldUtils.verifyValueBounds(fieldType, 30, 0, 59);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithValueBelowRange() {
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfDay();
        FieldUtils.verifyValueBounds(fieldType, -1, 0, 59);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithValueAboveRange() {
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfDay();
        FieldUtils.verifyValueBounds(fieldType, 60, 0, 59);
    }
    
    @Test
    public void testVerifyValueBoundsWithFieldName() {
        String fieldName = "Year";
        FieldUtils.verifyValueBounds(fieldName, 2021, 2000, 2100);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithInvalidValue() {
        String fieldName = "Year";
        FieldUtils.verifyValueBounds(fieldName, 1900, 2000, 2100);
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(11, 3, 10));
        assertEquals(10, FieldUtils.getWrappedValue(-3, 3, 10));
    }
    
    @Test
    public void testEqualsWithNull() {
        assertFalse(FieldUtils.equals("abc", null));
        assertFalse(FieldUtils.equals(null, "abc"));
        assertTrue(FieldUtils.equals(null, null));
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        assertTrue(FieldUtils.equals("abc", "abc"));
        assertTrue(FieldUtils.equals(5, 5));
    }
    
    @Test
    public void testEqualsWithUnequalObjects() {
        assertFalse(FieldUtils.equals("abc", "def"));
        assertFalse(FieldUtils.equals(5, 10));
    }
}