import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {

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
        int result = FieldUtils.safeAdd(5, 3);
        Assert.assertEquals(8, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddWithOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }

    @Test
    public void testSafeAddWithSignChange() {
        int result = FieldUtils.safeAdd(Integer.MAX_VALUE, -1);
        Assert.assertEquals(Integer.MAX_VALUE - 1, result);
    }

    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(10L, 3L);
        Assert.assertEquals(7L, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractWithOverflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
    }

    @Test
    public void testSafeMultiply() {
        int result = FieldUtils.safeMultiply(5, 3);
        Assert.assertEquals(15, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }

    @Test
    public void testSafeMultiplyWithLong() {
        long result = FieldUtils.safeMultiply(5L, 3L);
        Assert.assertEquals(15L, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithLongOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
    }

    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(100L);
        Assert.assertEquals(100, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToIntWithOverflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }

    @Test
    public void testSafeMultiplyToInt() {
        int result = FieldUtils.safeMultiplyToInt(5L, 3L);
        Assert.assertEquals(15, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntWithOverflow() {
        FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L);
    }

    @Test
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.secondOfDay(), 500, 0, 86399);
        FieldUtils.verifyValueBounds("seconds", 30, 0, 59);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithLowerBound() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 12, 1, 11);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithUpperBound() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.monthOfYear(), 15, 1, 12);
    }

    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(15, 1, 0, 10);
        Assert.assertEquals(6, result);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Object object1 = "test";
        Object object2 = "test";
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObjects() {
        Object object1 = null;
        Object object2 = null;
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        Object object1 = "test";
        Object object2 = "example";
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertFalse(result);
    }

}