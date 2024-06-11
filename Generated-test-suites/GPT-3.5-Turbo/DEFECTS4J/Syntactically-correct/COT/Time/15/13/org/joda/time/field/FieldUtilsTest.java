import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {

    // 
    
    @Test
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(5);
        Assert.assertEquals(-5, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeNegateWithMinValue() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSafeAdd() {
        int result = FieldUtils.safeAdd(2, 3);
        Assert.assertEquals(5, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeAddWithOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }
    
    @Test
    public void testSafeAddWithNegativeValues() {
        int result = FieldUtils.safeAdd(-2, -3);
        Assert.assertEquals(-5, result);
    }
    
    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(10, 5);
        Assert.assertEquals(5, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractWithOverflow() {
        FieldUtils.safeSubtract(Integer.MIN_VALUE, 1);
    }
    
    @Test
    public void testSafeMultiply() {
        int result = FieldUtils.safeMultiply(2, 3);
        Assert.assertEquals(6, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeMultiplyWithZero() {
        int result = FieldUtils.safeMultiply(5, 0);
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testSafeMultiplyWithNegativeValues() {
        long result = FieldUtils.safeMultiply(-3, -4);
        Assert.assertEquals(12, result);
    }
    
    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(100);
        Assert.assertEquals(100, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeToIntWithOverflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        int result = FieldUtils.safeMultiplyToInt(5, 2);
        Assert.assertEquals(10, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntWithOverflow() {
        FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 2);
    }
    
    @Test
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(new DateTimeFieldType("testType"), 10, 0, 20);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithValueBelowLowerBound() {
        FieldUtils.verifyValueBounds(new DateTimeFieldType("testType"), -10, 0, 20);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithValueAboveUpperBound() {
        FieldUtils.verifyValueBounds(new DateTimeFieldType("testType"), 30, 0, 20);
    }
    
    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(15, 2, 10, 20);
        Assert.assertEquals(17, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueWithMinValueEqualToMaxValue() {
        FieldUtils.getWrappedValue(15, 2, 20, 10);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        boolean result = FieldUtils.equals("abc", "abc");
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualsWithNotEqualObjects() {
        boolean result = FieldUtils.equals("abc", "def");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEqualsWithNullObjects() {
        boolean result = FieldUtils.equals(null, null);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualsWithOneNullObject() {
        boolean result = FieldUtils.equals("abc", null);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentTypes() {
        boolean result = FieldUtils.equals("abc", 123);
        Assert.assertFalse(result);
    }
    
    // 
    
}