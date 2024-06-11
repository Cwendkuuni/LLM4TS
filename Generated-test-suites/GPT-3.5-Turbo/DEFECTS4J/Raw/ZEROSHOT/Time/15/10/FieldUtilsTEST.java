import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {

    @Test(expected = ArithmeticException.class)
    public void testSafeNegate() {
        Assert.assertEquals(-5, FieldUtils.safeNegate(5));
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }

    @Test
    public void testSafeAdd() {
        Assert.assertEquals(10, FieldUtils.safeAdd(5, 5));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddOverflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }

    @Test
    public void testSafeAddLong() {
        Assert.assertEquals(10L, FieldUtils.safeAdd(5L, 5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddLongOverflow() {
        FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
    }

    @Test
    public void testSafeSubtract() {
        Assert.assertEquals(5L, FieldUtils.safeSubtract(10L, 5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractOverflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
    }

    @Test
    public void testSafeMultiply() {
        Assert.assertEquals(20, FieldUtils.safeMultiply(4, 5));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyOverflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test
    public void testSafeMultiplyLong() {
        Assert.assertEquals(20L, FieldUtils.safeMultiply(4L, 5L));
    }

    @Test
    public void testSafeMultiplyLongNegOne() {
        Assert.assertEquals(-4L, FieldUtils.safeMultiply(4L, -1));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongOverflow() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MAX_VALUE);
    }

    @Test
    public void testSafeMultiplyLongNegOneOverflow() {
        Assert.assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
    }

    @Test
    public void testSafeMultiplyLongOne() {
        Assert.assertEquals(4L, FieldUtils.safeMultiply(4L, 1));
    }

    @Test
    public void testSafeMultiplyLongZero() {
        Assert.assertEquals(0L, FieldUtils.safeMultiply(4L, 0));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongOverflow2() {
        FieldUtils.safeMultiply(Long.MIN_VALUE, -1);
    }

    @Test
    public void testSafeToInt() {
        Assert.assertEquals(5, FieldUtils.safeToInt(5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToIntOverflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }

    @Test
    public void testSafeMultiplyToInt() {
        Assert.assertEquals(20, FieldUtils.safeMultiplyToInt(4L, 5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(null, 5, 1, 10);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds2() {
        FieldUtils.verifyValueBounds(null, 5, 10, 1);
    }

    @Test
    public void testVerifyValueBounds3() {
        FieldUtils.verifyValueBounds(null, 5, 1, 10);
    }

    @Test
    public void testVerifyValueBounds4() {
        FieldUtils.verifyValueBounds(null, 5, 10, 10);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds5() {
        FieldUtils.verifyValueBounds(null, 5, 1, 4);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds6() {
        FieldUtils.verifyValueBounds(null, 5, 4, 1);
    }

    @Test
    public void testGetWrappedValue() {
        Assert.assertEquals(2, FieldUtils.getWrappedValue(7, -5, 3, 10));
    }

    @Test
    public void testGetWrappedValue2() {
        Assert.assertEquals(8, FieldUtils.getWrappedValue(3, 5, 3, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValue3() {
        FieldUtils.getWrappedValue(3, 5, 10, 3);
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(FieldUtils.equals("abc", "abc"));
        Assert.assertFalse(FieldUtils.equals("abc", "def"));
        Assert.assertFalse(FieldUtils.equals("abc", null));
        Assert.assertFalse(FieldUtils.equals(null, "abc"));
        Assert.assertTrue(FieldUtils.equals(null, null));
    }

}