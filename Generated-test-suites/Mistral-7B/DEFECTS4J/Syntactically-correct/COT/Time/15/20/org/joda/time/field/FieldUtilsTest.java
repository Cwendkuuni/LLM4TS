package org.joda.time.field;

import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertThrows;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    

    // Testing safeNegate method
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        int minValue = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(minValue));
    }

    // Testing safeAdd method for int values
    @Test
    public void testSafeAddIntInt() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        int overflowValue1 = Integer.MAX_VALUE;
        int overflowValue2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(overflowValue1, overflowValue2));

        int underflowValue1 = Integer.MIN_VALUE;
        int underflowValue2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(underflowValue1, underflowValue2));
    }

    // Testing safeAdd method for long values
    @Test
    public void testSafeAddLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8L, sum);

        long overflowValue1 = Long.MAX_VALUE;
        long overflowValue2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(overflowValue1, overflowValue2));

        long underflowValue1 = Long.MIN_VALUE;
        long underflowValue2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(underflowValue1, underflowValue2));
    }

    // Testing safeSubtract method for long values
    @Test
    public void testSafeSubtractLongLong() {
        long val1 = 10L;
        long val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, diff);

        long overflowValue1 = Long.MIN_VALUE;
        long overflowValue2 = 5L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(overflowValue1, overflowValue2));

        long underflowValue1 = Long.MIN_VALUE;
        long underflowValue2 = 5L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(underflowValue1, underflowValue2));
    }

    // Testing safeMultiply method for int values
    @Test
    public void testSafeMultiplyIntInt() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        int overflowValue1 = Integer.MIN_VALUE;
        int overflowValue2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(overflowValue1, overflowValue2));

        int underflowValue1 = Integer.MAX_VALUE;
        int underflowValue2 = -2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(underflowValue1, underflowValue2));
    }

    // Testing safeMultiply method for long and int values
    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        long overflowValue1 = Long.MIN_VALUE;
        int overflowValue2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(overflowValue1, overflowValue2));

        long underflowValue1 = Long.MAX_VALUE;
        int underflowValue2 = -2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(underflowValue1, underflowValue2));
    }

    // Testing safeMultiply method for long and long values
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        long overflowValue1 = Long.MIN_VALUE;
        long overflowValue2 = 2L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(overflowValue1, overflowValue2));

        long underflowValue1 = Long.MAX_VALUE;
        long underflowValue2 = -2L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(underflowValue1, underflowValue2));
    }

    // Testing safeToInt method
    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, intValue);

        long minValue = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(minValue));
    }

    // Testing verifyValueBounds method for DateTimeField
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType. millisecondOfDay(), "test");
        FieldUtils.verifyValueBounds(field, 50, 0, 59);

        FieldUtils.verifyValueBounds(field, 60, 0, 59);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 60, 0, 59));
    }

    // Testing verifyValueBounds method for DateTimeFieldType
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType. millisecondOfDay();
        FieldUtils.verifyValueBounds(fieldType, 50, 0, 59);

        FieldUtils.verifyValueBounds(fieldType, 60, 0, 59);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 60, 0, 59));
    }

    // Testing verifyValueBounds method for String
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "test";
        FieldUtils.verifyValueBounds(fieldName, 50, 0, 59);

        FieldUtils.verifyValueBounds(fieldName, 60, 0, 59);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 60, 0, 59));
    }

    // Testing getWrappedValue method for int values
    @Test
    public void testGetWrappedValueInt() {
        int value = 10;
        int wrapValue = 3;
        int minValue = 0;
        int maxValue = 5;

        int wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(3, wrappedValue);

        int negativeWrapValue = -3;
        int wrappedNegativeValue = FieldUtils.getWrappedValue(value, negativeWrapValue, minValue, maxValue);
        assertEquals(3, wrappedNegativeValue);
    }

    // Testing equals method
    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        Object obj3 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj3));

        Object obj4 = null;
        assertFalse(FieldUtils.equals(obj1, obj4));
    }

    
}