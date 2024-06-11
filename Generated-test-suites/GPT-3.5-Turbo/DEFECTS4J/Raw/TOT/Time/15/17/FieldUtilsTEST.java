import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        Assert.assertEquals(-10, FieldUtils.safeNegate(10));
        Assert.assertEquals(Integer.MAX_VALUE, FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeNegateWithMinValue() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSafeAdd() {
        Assert.assertEquals(15, FieldUtils.safeAdd(10, 5));
        Assert.assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 0));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeAddWithOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }
    
    @Test
    public void testSafeSubtract() {
        Assert.assertEquals(5, FieldUtils.safeSubtract(10, 5));
        Assert.assertEquals(-15, FieldUtils.safeSubtract(-10, 5));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractWithOverflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
    }
    
    @Test
    public void testSafeMultiply() {
        Assert.assertEquals(15, FieldUtils.safeMultiply(3, 5));
        Assert.assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }

    @Test
    public void testSafeMultiplyDifferentSigns() {
        Assert.assertEquals(-15, FieldUtils.safeMultiply(-3, 5));
        Assert.assertEquals(-15, FieldUtils.safeMultiply(3, -5));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongWithOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeToInt() {
        Assert.assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeToIntWithOverflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        Assert.assertEquals(25, FieldUtils.safeMultiplyToInt(5, 5));
        Assert.assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntWithOverflow() {
        FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 2);
    }
    
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        FieldUtils.verifyValueBounds(fieldType, 32, 1, 31);
    }
    
    @Test
    public void testVerifyValueBoundsWithinBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        FieldUtils.verifyValueBounds(fieldType, 15, 1, 31);
    }
    
    @Test
    public void testVerifyValueBoundsString() {
        FieldUtils.verifyValueBounds("DayOfMonth", 15, 1, 31);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueWithInvalidBounds() {
        FieldUtils.getWrappedValue(10, 20, 15, 15);
    }
    
    @Test
    public void testGetWrappedValue() {
        Assert.assertEquals(8, FieldUtils.getWrappedValue(3, 5, 1, 10));
        Assert.assertEquals(7, FieldUtils.getWrappedValue(10, -1, 1, 10));
        Assert.assertEquals(6, FieldUtils.getWrappedValue(15, -4, 1, 10));
        Assert.assertEquals(4, FieldUtils.getWrappedValue(-1, 5, 1, 10));
    }
    
    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertTrue(FieldUtils.equals(obj1, obj1));
        Assert.assertFalse(FieldUtils.equals(obj1, obj2));
        Assert.assertFalse(FieldUtils.equals(null, obj2));
    }
}