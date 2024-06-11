import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.junit.Assert;

public class FieldUtilsTest {

    @ org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        Assert.assertEquals("Negate test failed", expected, result);

        value = Integer.MIN_VALUE;
        Assert.assertThrows("Negate test with minimum int value", ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @ org.junit.Test
    public void testSafeAddIntInt() {
        int val1 = 3;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        int expected = 5;
        Assert.assertEquals("Add test failed", expected, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        Assert.assertThrows("Add test with minimum int value", ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @ org.junit.Test
    public void testSafeAddLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long sum = FieldUtils.safeAdd(val1, val2);
        long expected = 8L;
        Assert.assertEquals("Add test failed", expected, sum);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        Assert.assertThrows("Add test with minimum long value", ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @ org.junit.Test
    public void testSafeSubtractLongLong() {
        long val1 = 10L;
        long val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        long expected = 7L;
        Assert.assertEquals("Subtract test failed", expected, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        Assert.assertThrows("Subtract test with minimum long value", ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @ org.junit.Test
    public void testSafeMultiplyIntInt() {
        int val1 = 3;
        int val2 = 2;
        int product = FieldUtils.safeMultiply(val1, val2);
        int expected = 6;
        Assert.assertEquals("Multiply test failed", expected, product);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        Assert.assertThrows("Multiply test with minimum int values", ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @ org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 2;
        long product = FieldUtils.safeMultiply(val1, val2);
        long expected = 10L;
        Assert.assertEquals("Multiply test failed", expected, product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        Assert.assertThrows("Multiply test with minimum long value", ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @ org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long product = FieldUtils.safeMultiply(val1, val2);
        long expected = 15L;
        Assert.assertEquals("Multiply test failed", expected, product);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        Assert.assertThrows("Multiply test with minimum long values", ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @ org.junit.Test
    public void testSafeToInt() {
        long value = 12345;
        int expected = 12345;
        int result = FieldUtils.safeToInt(value);
        Assert.assertEquals("ToInt test failed", expected, result);

        value = Long.MAX_VALUE;
        Assert.assertThrows("ToInt test with maximum long value", ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @ org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        int expected = 15;
        Assert.assertEquals("MultiplyToInt test failed", expected, product);

        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE;
        Assert.assertThrows("MultiplyToInt test with minimum and maximum long values", ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @ org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldIntIntInt() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", null);
        int value = 31;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 32;
        Assert.assertThrows("VerifyValueBounds test with value outside bounds", IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 1, 31));
    }

    @ org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldTypeIntIntInt() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 31;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        value = 32;
        Assert.assertThrows("VerifyValueBounds test with value outside bounds", IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 1, 31));
    }

    @ org.junit.Test
    public void testVerifyValueBoundsStringIntIntInt() {
        String fieldName = "dayOfMonth";
        int value = 31;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        value = 32;
        Assert.assertThrows("VerifyValueBounds test with value outside bounds", IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 1, 31));
    }

    @ org.junit.Test
    public void testGetWrappedValueIntIntInt() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        int expected = 5;
        Assert.assertEquals("GetWrappedValue test failed", expected, wrappedValue);

        value = 4;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        expected = 5;
        Assert.assertEquals("GetWrappedValue test failed", expected, wrappedValue);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        expected = 1;
        Assert.assertEquals("GetWrappedValue test failed", expected, wrappedValue);
    }

    @ org.junit.Test
    public void testGetWrappedValueIntIntIntNegative() {
        int value = -5;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        int expected = 1;
        Assert.assertEquals("GetWrappedValue test failed", expected, wrappedValue);

        value = -6;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        expected = 5;
        Assert.assertEquals("GetWrappedValue test failed", expected, wrappedValue);
    }

    @ org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertFalse("Equals test failed", FieldUtils.equals(obj1, obj2));

        Object obj3 = obj1;
        Assert.assertTrue("Equals test failed", FieldUtils.equals(obj1, obj3));

        obj1 = null;
        Assert.assertFalse("Equals test failed", FieldUtils.equals(obj1, obj2));
    }
}