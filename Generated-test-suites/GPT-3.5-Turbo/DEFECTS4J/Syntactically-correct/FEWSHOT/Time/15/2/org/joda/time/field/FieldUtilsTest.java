package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {
    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertEquals(1, FieldUtils.safeNegate(-1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeNegate_ThrowsArithmeticException() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        assertEquals(4, FieldUtils.safeAdd(1, 3));
        assertEquals(-4, FieldUtils.safeAdd(-1, -3));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE - 1, 1));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_ThrowsArithmeticException() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        assertEquals(16, FieldUtils.safeMultiply(4, 4));
        assertEquals(-16, FieldUtils.safeMultiply(-4, 4));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiply_ThrowsArithmeticException() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(0, FieldUtils.safeToInt(0));
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(-1, FieldUtils.safeToInt(-1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSafeToInt_ThrowsArithmeticException() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(0, FieldUtils.getWrappedValue(0, 0, 0));
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 1));
        assertEquals(0, FieldUtils.getWrappedValue(2, 0, 1));
        assertEquals(-1, FieldUtils.getWrappedValue(3, -1, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValue_ThrowsIllegalArgumentException() {
        FieldUtils.getWrappedValue(0, 1, 0);
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, new Object()));
        assertFalse(FieldUtils.equals(new Object(), null));
        assertTrue(FieldUtils.equals(new Object(), new Object()));
    }
    
    @Test
    public void testVerifyValueBounds_DateTimeField() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfWeek(), 10, 0, 7);
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.dayOfWeek(), e.getFieldType());
            assertEquals(10, e.getIllegalNumberValue().intValue());
            assertEquals(0, e.getLowerBound().intValue());
            assertEquals(7, e.getUpperBound().intValue());
        }
    }
    
    @Test
    public void testVerifyValueBounds_DateTimeFieldType() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.era(), 10, 0, 7);
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.era(), e.getFieldType());
            assertEquals(10, e.getIllegalNumberValue().intValue());
            assertEquals(0, e.getLowerBound().intValue());
            assertEquals(7, e.getUpperBound().intValue());
        }
    }
    
    @Test
    public void testVerifyValueBounds_String() {
        try {
            FieldUtils.verifyValueBounds("foo", 10, 0, 7);
        } catch (IllegalFieldValueException e) {
            assertEquals("foo", e.getFieldName());
            assertEquals(10, e.getIllegalNumberValue().intValue());
            assertEquals(0, e.getLowerBound().intValue());
            assertEquals(7, e.getUpperBound().intValue());
        }
    }
}