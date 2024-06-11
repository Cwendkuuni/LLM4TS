package org.joda.time.field;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertEquals(DurationFieldType.centuries(), field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertEquals("centuries", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getValue(123456789L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getValueAsLong(123456789L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getValue(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getValueAsLong(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getMillis(456);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getMillis(123456789L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getMillis(456, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getMillis(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.add(123456789L, 456);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.add(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getDifference(123456789L, 987654321L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        field.getDifferenceAsLong(123456789L, 987654321L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(1, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(field1.hashCode(), field2.hashCode());
        assertNotEquals(field1.hashCode(), field3.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertEquals("UnsupportedDurationField[centuries]", field.toString());
    }
    
    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        UnsupportedDurationField field2 = field1.readResolve();
        assertEquals(field1, field2);
    }
}