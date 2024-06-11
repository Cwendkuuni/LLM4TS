import org.junit.Assert;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test(expected = ArithmeticException.class)
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value);
    }

    @Test
    public void testSafeNegate_positiveValue() {
        int value = 10;
        int result = FieldUtils.safeNegate(value);
        Assert.assertEquals(-value, result);
    }

    @Test
    public void testSafeAdd() {
        int val1 = 100;
        int val2 = 200;
        int result = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(val1 + val2, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_overflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 200;
        FieldUtils.safeAdd(val1, val2);
    }

    @Test
    public void testSafeAdd_negativeValues() {
        int val1 = -100;
        int val2 = -200;
        int result = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(val1 + val2, result);
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 10;
        int val2 = 20;
        int result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiply_overflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 200;
        FieldUtils.safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiply_longValue() {
        long val1 = 1000000000;
        int val2 = 2;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiply_longValue_overflow() {
        long val1 = Long.MAX_VALUE;
        int val2 = 2;
        FieldUtils.safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiply_longValues() {
        long val1 = 10000000000000000L;
        long val2 = 2000000L;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 1000L;
        long val2 = 200L;
        long result = FieldUtils.safeSubtract(val1, val2);
        Assert.assertEquals(val1 - val2, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtract_overflow() {
        long val1 = Long.MIN_VALUE;
        long val2 = 1000L;
        FieldUtils.safeSubtract(val1, val2);
    }

    @Test
    public void testSafeToInt() {
        long value = Integer.MAX_VALUE;
        int result = FieldUtils.safeToInt(value);
        Assert.assertEquals((int) value, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToInt_overflow() {
        long value = Long.MAX_VALUE;
        FieldUtils.safeToInt(value);
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 1000L;
        long val2 = 20L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        Assert.assertEquals((int) (val1 * val2), result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToInt_overflow() {
        long val1 = Long.MAX_VALUE;
        long val2 = 20L;
        FieldUtils.safeMultiplyToInt(val1, val2);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 3;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_invalidValue() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 10;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyValueBounds_invalidBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 3;
        int lowerBound = 7;
        int upperBound = 1;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test
    public void testGetWrappedValue() {        
        int currentValue = 5;
        int wrapValue = -9;
        int minValue = 1;
        int maxValue = 10;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals((currentValue + wrapValue - minValue) % (maxValue - minValue + 1) + minValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValue_invalidBounds() {
        int currentValue = 5;
        int wrapValue = -9;
        int minValue = 10;
        int maxValue = 1;
        FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
    }

    @Test
    public void testEquals() {
        Object obj1 = "Hello";
        Object obj2 = "Hello";
        boolean result = FieldUtils.equals(obj1, obj2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_nullValues() {
        Object obj1 = null;
        Object obj2 = null;
        boolean result = FieldUtils.equals(obj1, obj2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_differentValues() {
        Object obj1 = "Hello";
        Object obj2 = "World";
        boolean result = FieldUtils.equals(obj1, obj2);
        Assert.assertFalse(result);
    }

}