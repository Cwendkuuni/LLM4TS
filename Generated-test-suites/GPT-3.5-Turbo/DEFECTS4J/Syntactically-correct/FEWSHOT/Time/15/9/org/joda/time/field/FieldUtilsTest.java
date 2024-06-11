
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Before;
import org.junit.Test;

public class FieldUtilsTest {
    
    @Test
    public void testSafeNegate() {
        assertEquals(-10, FieldUtils.safeNegate(10));
        assertEquals(0, FieldUtils.safeNegate(0));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(15, FieldUtils.safeAdd(10, 5));
        assertEquals(-10, FieldUtils.safeAdd(-5, -5));
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 2147483647 + 1", e.getMessage());
        }
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: -2147483648 + -1", e.getMessage());
        }
    }
    
    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertEquals(0, FieldUtils.safeSubtract(-5, -5));
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        try {
            FieldUtils.safeSubtract(Integer.MAX_VALUE, -1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 2147483647 - -1", e.getMessage());
        }
        try {
            FieldUtils.safeSubtract(Integer.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: -2147483648 - 1", e.getMessage());
        }
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(50, FieldUtils.safeMultiply(10, 5));
        assertEquals(25, FieldUtils.safeMultiply(-5, -5));
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: 2147483647 * 2", e.getMessage());
        }
        try {
            FieldUtils.safeMultiply(Integer.MIN_VALUE, -1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: -2147483648 * -1", e.getMessage());
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(15, FieldUtils.getWrappedValue(10, 5, 0, 20));
        assertEquals(5, FieldUtils.getWrappedValue(-5, 10, -10, 10));
        assertEquals(0, FieldUtils.getWrappedValue(0, 1, 0, 1));
        assertEquals(-5, FieldUtils.getWrappedValue(-5, 10, -10, 10));
        assertEquals(-3, FieldUtils.getWrappedValue(-8, 5, -10, 10));
        assertEquals(0, FieldUtils.getWrappedValue(-10, 10, -10, 10));
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertTrue(FieldUtils.equals("field", "field"));
        assertFalse(FieldUtils.equals(null, "field"));
        assertFalse(FieldUtils.equals("field", null));
        assertFalse(FieldUtils.equals("field1", "field2"));
    }
}
