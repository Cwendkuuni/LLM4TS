package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    private UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.YEARS, field.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("Years", field.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        field.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        field.getValueAsLong(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        field.getValue(100, 150);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        field.getValueAsLong(100L, 150L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        field.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        field.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntWithInstant() {
        field.getMillis(100, 150L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongWithInstant() {
        field.getMillis(100L, 150L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInt() {
        field.add(100L, 50);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        field.add(100L, 50L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceInt() {
        field.getDifference(200L, 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLong() {
        field.getDifferenceAsLong(200L, 100L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        
        assertTrue(field.compareTo(field2) > 0);
        assertTrue(field2.compareTo(field) < 0);
        assertTrue(field.compareTo(field3) == 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        
        assertTrue(field.equals(field));
        assertTrue(field.equals(field2));
        assertFalse(field.equals(field3));
        assertFalse(field.equals(null));
        assertFalse(field.equals("Years"));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MONTHS);
        
        assertEquals(field.hashCode(), field.hashCode());
        assertEquals(field.hashCode(), field2.hashCode());
        assertNotEquals(field.hashCode(), field3.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[Years]", field.toString());
    }

}