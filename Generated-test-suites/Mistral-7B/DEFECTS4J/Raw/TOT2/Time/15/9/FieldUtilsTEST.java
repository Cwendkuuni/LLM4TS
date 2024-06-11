import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldUtilsTest {

    @Test
    public void testSafeNegateInt() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAddIntInt() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeAddLongInt() {
        long val1 = 5L;
        long val2 = 3L;
        long sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8L, sum);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtractLongLong() {
        long diff;
        long val1 = 10L;
        long val2 = 3L;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, diff);

        val1 = 10L;
        val2 = 8L;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(2L, diff);

        val1 = 10L;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = 10L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiplyIntInt() {
        int result = FieldUtils.safeMultiply(3, 4);
        assertEquals(12, result);

        int val1 = Integer.MIN_VALUE;
        int val2 = 5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long result = FieldUtils.safeMultiply(3L, 4L);
        assertEquals(12L, result);

        long val1 = Long.MIN_VALUE;
        int val2 = 5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5L;
        int val3 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val3));

        val1 = Long.MIN_VALUE;
        long val4 = 5L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val4));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long result = FieldUtils.safeMultiply(3L, 4L);
        assertEquals(12L, result);

        long val1 = Long.MIN_VALUE;
        long val2 = 5L;
        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(Long.MIN_VALUE, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeToIntLong() {
        int result = FieldUtils.safeToInt(10L);
        assertEquals(10, result);

        long val1 = Integer.MIN_VALUE - 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(val1));

        long val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(val2));
    }

    @Test
    public void testSafeMultiplyToIntLongLong() {
        int result = FieldUtils.safeMultiplyToInt(3L, 4L);
        assertEquals(12, result);

        long val1 = Long.MIN_VALUE - 1;
        long val2 = 4L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));

        long val3 = Long.MIN_VALUE;
        long val4 = 4L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val3, val4));

        long val5 = 3L;
        long val6 = Long.MIN_VALUE - 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val5, val6));
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldIntInt() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31);

        int value = 1;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 1, 31));
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldTypeIntInt() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        int value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 1, 31));
    }

    @Test
    public void testVerifyValueBoundsStringIntInt() {
        String fieldName = "dayOfMonth";

        int value = 1;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 1, 31));
    }

    @Test
    public void testGetWrappedValueIntIntInt() {
        int value = 10;
        int minValue = 1;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, -2, minValue, maxValue);
        assertEquals(3, wrappedValue);
    }

    @Test
    public void testGetWrappedValueIntIntIntNegativeMin() {
        int value = 10;
        int minValue = 6;
        int maxValue = 5;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, -2, minValue, maxValue));
    }

    @Test
    public void testGetWrappedValueIntIntIntNegativeMax() {
        int value = 10;
        int minValue = 1;
        int maxValue = 2;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, -2, minValue, maxValue));
    }

    @Test
    public void testGetWrappedValueIntIntIntMinGreaterThanMax() {
        int value = 10;
        int minValue = 6;
        int maxValue = 2;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, -2, minValue, maxValue));
    }

    @Test
    public void testGetWrappedValueIntIntIntMinEqualsMax() {
        int value = 10;
        int minValue = 5;
        int maxValue = 5;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, -2, minValue, maxValue));
    }

    @Test
    public void testGetWrappedValueIntIntIntMinEqualsZero() {
        int value = 10;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, -2, minValue, maxValue);
        assertEquals(3, wrappedValue);
    }

    @Test
    public void testGetWrappedValueIntIntIntMaxNegative() {
        int value = 10;
        int minValue = 1;
        int maxValue = -2;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, -2, minValue, maxValue));
    }

    @Test
    public void testGetWrappedValueIntIntIntMaxEqualsMinusMinusOne() {
        int value = 10;
        int minValue = 1;
        int maxValue = 1 - 1;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, -2, minValue, maxValue));
    }

    @Test
    public void testEqualsObjectObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = new Object();
        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        obj2 = null;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = new Object();
        assertThrows(NullPointerException.class, () -> FieldUtils.equals(obj1, null));
    }
}