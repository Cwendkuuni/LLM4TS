package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {
    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegate_MinValue() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE - 1, 1));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_MaxValue() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testSafeMultiply() {
        assertEquals(6, FieldUtils.safeMultiply(2, 3));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE / 2, 2));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiply_MaxValue() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testSafeToInt() {
        assertEquals(10, FieldUtils.safeToInt(10));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToInt_TooBig() {
        FieldUtils.safeToInt(Integer.MAX_VALUE + 1L);
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 12, 0, 23);
        FieldUtils.verifyValueBounds("Field", 10, 0, 15);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_OutOfBounds() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 2020, 2000, 2010);
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, "test"));
        assertFalse(FieldUtils.equals("test", null));
        assertTrue(FieldUtils.equals("test", "test"));
    }

    // Additional test cases for other methods can be added here

}