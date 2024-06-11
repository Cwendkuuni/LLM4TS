package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        assertTrue(field1 instanceof UnsupportedDurationField);
        assertTrue(field2 instanceof UnsupportedDurationField);
        assertEquals(DurationFieldType.millis(), field1.getType());
        assertEquals(DurationFieldType.seconds(), field2.getType());
    }
    
    @Test
    public void testGetType() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertEquals(DurationFieldType.millis(), field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertEquals("millis", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getValue(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getValueAsLong(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getValue(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getValueAsLong(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getMillis(1000, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getMillis(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.add(1000L, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.add(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getDifference(2000L, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        field.getDifferenceAsLong(2000L, 1000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        assertTrue(field2.compareTo(field1) > 0);
        assertEquals(0, field1.compareTo(field1));
        assertEquals(0, field2.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        DurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertTrue(field1.equals(field1));
        assertFalse(field1.equals(field2));
        assertTrue(field1.equals(field3));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals("field1"));
    }
    
    @Test
    public void testHashCode() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        assertEquals(field1.getName().hashCode(), field1.hashCode());
        assertEquals(field2.getName().hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }
    
}