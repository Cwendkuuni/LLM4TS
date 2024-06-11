package org.joda.time.field;

import org.joda.time.DurationFieldType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;


public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertTrue(field.isSupported());
        assertEquals(DurationFieldType.YEARS, field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertEquals("years", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertTrue(field.isPrecise());
    }
    
    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getValue(1000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getValueAsLong(1000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getValue(1000, 2000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueWithInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getValueAsLong(1000, 2000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getMillis(1000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillisWithValueLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getMillis(1000L);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getMillis(1000, 2000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetMillisWithValueAndInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getMillis(1000, 2000L);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.add(1000, 2000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testAddWithInstantAndValueLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.add(1000, 2000L);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getDifference(1000, 2000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        try {
            field.getDifferenceAsLong(1000, 2000);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("years field is unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertEquals(0, field.compareTo(field));
    }
}