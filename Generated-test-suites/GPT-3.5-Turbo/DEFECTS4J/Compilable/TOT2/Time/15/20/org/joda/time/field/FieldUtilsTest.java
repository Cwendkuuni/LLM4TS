package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException should have been thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException should have been thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(25, FieldUtils.safeMultiply(5, 5));
        assertEquals(0, FieldUtils.safeMultiply(0, 5));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("ArithmeticException should have been thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertEquals(0, FieldUtils.safeSubtract(5, 5));
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("ArithmeticException should have been thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("ArithmeticException should have been thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(2, FieldUtils.getWrappedValue(10, -8, 5));
        assertEquals(5, FieldUtils.getWrappedValue(2, -8, 5));
        try {
            FieldUtils.getWrappedValue(5, 10, 5);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals("test", null));
        assertFalse(FieldUtils.equals(null, "test"));
        assertTrue(FieldUtils.equals("test", "test"));
    }

}