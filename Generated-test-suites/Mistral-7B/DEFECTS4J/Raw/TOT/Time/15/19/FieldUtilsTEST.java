package org.joda.time.field.util; // Import necessary classes here if needed

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 10;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-10, result);

        value = -10;
        result = FieldUtils.safeNegate(value);
        assertEquals(10, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5, val2 = 3, sum;

        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1, sum); // No overflow

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 5, val2 = 3, diff;

        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(2, diff);

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 2, val2 = 3, result;

        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1, result); // No overflow
    }

    @Test
    public void testSafeMultiplyLongWithInt() {
        long val1 = 2, val2 = 3;

        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongWithLong() {
        long val1 = 2, val2 = 3, result;

        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        val1 = 1;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = 3;
        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1, result); // No overflow
    }

    @Test
    public void testSafeToInt() {
        long value = 12345;
        int result = FieldUtils.safeToInt(value);
        assertEquals(12345, result);

        value = Integer.MIN_VALUE + 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 2, val2 = 3, result;

        result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(6, result);

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(val1, result); // No overflow
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", 1);

        assertFalse(FieldUtils.verifyValueBounds(field, 0, 0, 7));
        assertFalse(FieldUtils.verifyValueBounds(field, 8, 0, 7));
        assertTrue(FieldUtils.verifyValueBounds(field, 1, 0, 7));
        assertTrue(FieldUtils.verifyValueBounds(field, 7, 0, 7));

        field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", 30);

        assertFalse(FieldUtils.verifyValueBounds(field, 0, 1, 31));
        assertFalse(FieldUtils.getWrappedValue(0, 1, 1, 31) == 0);
        assertEquals(1, FieldUtils.getWrappedValue(1, 1, 1, 31));
        assertEquals(31, FieldUtils.getWrappedValue(30, 1, 1, 31));

        assertFalse(FieldUtils.verifyValueBounds(field, -1, 1, 31));
        assertEquals(30, FieldUtils.getWrappedValue(-1, 1, 1, 31));

        assertFalse(FieldUtils.verifyValueBounds(field, 32, 1, 31));
        assertEquals(1, FieldUtils.getWrappedValue(32, 1, 1, 31)); // The wrapped value is 1
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;

        assertTrue(FieldUtils.equals(obj1, obj1));
        assertTrue(FieldUtils.equals(obj1, obj3));
        assertFalse(FieldUtils.equals(obj1, obj2));
        assertFalse(FieldUtils.equals(obj1, null));
    }
}