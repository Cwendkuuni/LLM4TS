import org.junit.Assert;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(5);
        Assert.assertEquals(-5, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeNegateMinValue() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSafeAdd() {
        int result = FieldUtils.safeAdd(5, 3);
        Assert.assertEquals(8, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeAddOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }
    
    @Test
    public void testSafeAddLong() {
        long result = FieldUtils.safeAdd(5L, 3L);
        Assert.assertEquals(8L, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeAddLongOverflow() {
        FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
    }
    
    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(8L, 3L);
        Assert.assertEquals(5L, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractOverflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
    }
    
    @Test
    public void testSafeMultiplyInt() {
        int result = FieldUtils.safeMultiply(5, 3);
        Assert.assertEquals(15, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyIntOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeMultiplyLongInt() {
        long result = FieldUtils.safeMultiply(5L, 3);
        Assert.assertEquals(15L, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongIntOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeMultiplyLong() {
        long result = FieldUtils.safeMultiply(5L, 3L);
        Assert.assertEquals(15L, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
    }
    
    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(5L);
        Assert.assertEquals(5, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeToIntOverflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        int result = FieldUtils.safeMultiplyToInt(5L, 3L);
        Assert.assertEquals(15, result);
    }
    
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField();
        try {
            FieldUtils.verifyValueBounds(field, 5, 1, 10);
            // No exception, test passed
        } catch (IllegalFieldValueException e) {
            Assert.fail("Unexpected IllegalFieldValueException");
        }
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsDateTimeFieldLowerBound() {
        DateTimeField field = new DateTimeField();
        FieldUtils.verifyValueBounds(field, 0, 1, 10);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsDateTimeFieldUpperBound() {
        DateTimeField field = new DateTimeField();
        FieldUtils.verifyValueBounds(field, 11, 1, 10);
    }
    
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = new DateTimeFieldType();
        try {
            FieldUtils.verifyValueBounds(fieldType, 5, 1, 10);
            // No exception, test passed
        } catch (IllegalFieldValueException e) {
            Assert.fail("Unexpected IllegalFieldValueException");
        }
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsDateTimeFieldTypeLowerBound() {
        DateTimeFieldType fieldType = new DateTimeFieldType();
        FieldUtils.verifyValueBounds(fieldType, 0, 1, 10);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsDateTimeFieldTypeUpperBound() {
        DateTimeFieldType fieldType = new DateTimeFieldType();
        FieldUtils.verifyValueBounds(fieldType, 11, 1, 10);
    }
    
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "Test Field";
        try {
            FieldUtils.verifyValueBounds(fieldName, 5, 1, 10);
            // No exception, test passed
        } catch (IllegalFieldValueException e) {
            Assert.fail("Unexpected IllegalFieldValueException");
        }
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsStringLowerBound() {
        String fieldName = "Test Field";
        FieldUtils.verifyValueBounds(fieldName, 0, 1, 10);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsStringUpperBound() {
        String fieldName = "Test Field";
        FieldUtils.verifyValueBounds(fieldName, 11, 1, 10);
    }
    
    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(5, 3, 1, 10);
        Assert.assertEquals(8, result);
    }
    
    @Test
    public void testEquals() {
        boolean result1 = FieldUtils.equals("abc", "abc");
        Assert.assertTrue(result1);
        
        boolean result2 = FieldUtils.equals("abc", "def");
        Assert.assertFalse(result2);
        
        boolean result3 = FieldUtils.equals("abc", null);
        Assert.assertFalse(result3);
        
        boolean result4 = FieldUtils.equals(null, null);
        Assert.assertTrue(result4);
    }
}