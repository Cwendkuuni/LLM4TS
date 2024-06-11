import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(10);
        Assert.assertEquals(-10, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateWithException() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }

    @Test
    public void testSafeAdd() {
        int result = FieldUtils.safeAdd(10, 5);
        Assert.assertEquals(15, result);
    }

    @Test
    public void testSafeAddWithNegativeValues() {
        int result = FieldUtils.safeAdd(-10, -5);
        Assert.assertEquals(-15, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddWithOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }

    @Test
    public void testSafeMultiply() {
        int result = FieldUtils.safeMultiply(10, 5);
        Assert.assertEquals(50, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 5);
    }

    @Test
    public void testSafeMultiplyWithLong() {
        long result = FieldUtils.safeMultiply(10000000000L, 5);
        Assert.assertEquals(50000000000L, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithLongOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 5);
    }

    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(10, 5);
        Assert.assertEquals(5, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractWithOverflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
    }

    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(Long.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToIntWithOverflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE + 1);
    }

    @Test
    public void testSafeMultiplyToInt() {
        int result = FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 5);
        Assert.assertEquals(Integer.MAX_VALUE, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntWithOverflow() {
        FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 10);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = null; // Set your DateTimeField object here
        try {
            FieldUtils.verifyValueBounds(field, 10, 0, 20);
        } catch (IllegalFieldValueException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithException() {
        DateTimeField field = null; // Set your DateTimeField object here
        FieldUtils.verifyValueBounds(field, 25, 0, 20);
    }

    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(10, 5, 0, 20);
        Assert.assertEquals(15, result);
    }

    @Test
    public void testGetWrappedValueWithNegativeValues() {
        int result = FieldUtils.getWrappedValue(-10, -5, -20, 0);
        Assert.assertEquals(-5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueWithIllegalArgumentException() {
        FieldUtils.getWrappedValue(10, 5, 20, 0);
    }

    @Test
    public void testEquals() {
        boolean result = FieldUtils.equals("abc", "abc");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testEqualsWithNulls() {
        boolean result = FieldUtils.equals(null, null);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testEqualsWithOneNull() {
        boolean result = FieldUtils.equals("abc", null);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        boolean result = FieldUtils.equals("abc", "def");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testEqualsWithDifferentTypes() {
        boolean result = FieldUtils.equals("abc", 123);
        Assert.assertEquals(false, result);
    }

}