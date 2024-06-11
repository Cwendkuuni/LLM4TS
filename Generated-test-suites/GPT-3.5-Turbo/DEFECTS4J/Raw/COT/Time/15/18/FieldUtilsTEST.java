import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        Assert.assertEquals(-5, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateMinValue() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value);
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(8, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 1;
        FieldUtils.safeAdd(val1, val2);
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(15, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 2;
        FieldUtils.safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        Assert.assertEquals(15, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntOverflow() {
        long val1 = Long.MAX_VALUE;
        long val2 = 10L;
        FieldUtils.safeMultiplyToInt(val1, val2);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 4;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsOutsideRange() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 8;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = 10;
        int wrapValue = -15;
        int minValue = 1;
        int maxValue = 20;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(16, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueInvalidRange() {
        int currentValue = 10;
        int wrapValue = -15;
        int minValue = 21;
        int maxValue = 20;
        FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
    }

    @Test
    public void testEquals() {
        Object object1 = "hello";
        Object object2 = "hello";
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsBothNull() {
        Object object1 = null;
        Object object2 = null;
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsOneNull() {
        Object object1 = "hello";
        Object object2 = null;
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertFalse(result);
    }
}