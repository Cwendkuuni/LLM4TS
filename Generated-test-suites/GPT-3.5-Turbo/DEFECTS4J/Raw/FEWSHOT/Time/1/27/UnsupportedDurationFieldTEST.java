package org.joda.time.field;

import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        assertEquals(UnsupportedDurationField.class, UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getClass());
    }
    
    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.YEARS, UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Years", UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getName());
    }
    
    @Test
    public void testIsSupported() {
        assertFalse(UnsupportedDurationField.getInstance(DurationFieldType.YEARS).isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        assertTrue(UnsupportedDurationField.getInstance(DurationFieldType.YEARS).isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getValue(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getValueAsLong(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getMillis(1000, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getMillis(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).add(1000L, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd2() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).add(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getDifference(1000L, 2000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getDifferenceAsLong(1000L, 2000L);
    }
    
    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertTrue(durationField.compareTo(durationField) == 0);
        assertTrue(durationField.compareTo(new UnsupportedDurationField(DurationFieldType.HOURS)) == 1);
    }
    
    @Test
    public void testEquals() {
        DurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertTrue(durationField.equals(durationField));
        assertFalse(durationField.equals(new UnsupportedDurationField(DurationFieldType.HOURS)));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(UnsupportedDurationField.getInstance(DurationFieldType.YEARS).getName().hashCode(),
                     UnsupportedDurationField.getInstance(DurationFieldType.YEARS).hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[Years]", UnsupportedDurationField.getInstance(DurationFieldType.YEARS).toString());
    }
}