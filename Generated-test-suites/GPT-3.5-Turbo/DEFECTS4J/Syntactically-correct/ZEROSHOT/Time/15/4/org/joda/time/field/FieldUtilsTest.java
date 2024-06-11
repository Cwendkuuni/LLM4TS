import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {
    
    @Test(expected=ArithmeticException.class)
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSafeAdd() {
        int result = FieldUtils.safeAdd(1, 2);
        assertEquals(3, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeAddOverflow() {
        int result = FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }
    
    @Test
    public void testSafeAddLong() {
        long result = FieldUtils.safeAdd(1L, 2L);
        assertEquals(3L, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeAddLongOverflow() {
        long result = FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
    }
    
    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(5L, 3L);
        assertEquals(2L, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeSubtractOverflow() {
        long result = FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
    }
    
    @Test
    public void testSafeMultiplyInt() {
        int result = FieldUtils.safeMultiply(2, 3);
        assertEquals(6, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeMultiplyIntOverflow() {
        int result = FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeMultiplyLong() {
        long result = FieldUtils.safeMultiply(2L, 3L);
        assertEquals(6L, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeMultiplyLongOverflow() {
        long result = FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
    }
    
    @Test
    public void testSafeMultiplyLongWithZero() {
        long result = FieldUtils.safeMultiply(0L, 3L);
        assertEquals(0L, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeMultiplyLongWithOverflow() {
        long result = FieldUtils.safeMultiply(Long.MIN_VALUE, -1L);
    }
    
    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeToIntOverflow() {
        int result = FieldUtils.safeToInt(Long.MAX_VALUE);
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        int result = FieldUtils.safeMultiplyToInt(2L, 3L);
        assertEquals(6, result);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testSafeMultiplyToIntOverflow() {
        int result = FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L);
    }
    
    @Test
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfMonth(), 5, 1, 31);
    }
    
    @Test(expected=IllegalFieldValueException.class)
    public void testVerifyValueBoundsOutsideRange() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.monthOfYear(), 15, 1, 12);
    }
    
    @Test
    public void testVerifyValueBoundsWithFieldName() {
        FieldUtils.verifyValueBounds("Day", 5, 1, 31);
    }
    
    @Test(expected=IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithFieldNameOutsideRange() {
        FieldUtils.verifyValueBounds("Month", 15, 1, 12);
    }
    
    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(5, 3, 1, 10);
        assertEquals(8, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetWrappedValueMinGreaterOrEqualToMax() {
        int result = FieldUtils.getWrappedValue(5, 3, 10, 1);
    }
    
    @Test
    public void testEquals() {
        boolean result = FieldUtils.equals("Test", "Test");
        assertTrue(result);
    }
    
    @Test
    public void testEqualsNull() {
        boolean result = FieldUtils.equals(null, "Test");
        assertFalse(result);
    }
    
    @Test
    public void testEqualObjects() {
        Object obj = new Object();
        boolean result = FieldUtils.equals(obj, obj);
        assertTrue(result);
    }
    
    @Test
    public void testUnequalObjects() {
        boolean result = FieldUtils.equals("Test1", "Test2");
        assertFalse(result);
    }
    
}