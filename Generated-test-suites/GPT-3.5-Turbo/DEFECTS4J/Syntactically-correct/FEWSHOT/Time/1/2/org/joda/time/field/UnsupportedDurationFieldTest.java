package org.joda.time.field;

import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals("YEAR", field1.getType().getName());
        
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MONTH);
        assertEquals("MONTH", field2.getType().getName());
        
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.DAY);
        assertEquals("DAY", field3.getType().getName());
    }
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals(DurationFieldType.YEAR, field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals("YEAR", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getValue(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getValueAsLong(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getValue(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getValueAsLong(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getMillis(7);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getMillis(7, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getMillis(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.add(1000L, 7);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.add(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getDifference(2000L, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        field.getDifferenceAsLong(2000L, 1000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MONTH);
        assertEquals(1, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals(field1, field2);
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals(field1.hashCode(), field2.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEAR);
        assertEquals("UnsupportedDurationField[YEAR]", field.toString());
    }
}