package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetInstanceNull() {
        assertNull(UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals("hours", field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueInt() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getValue(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getValueAsLong(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getMillis(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.add(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getDifference(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        field.getDifferenceAsLong(1, 1);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.HOURS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.HOURS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.HOURS);
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        int hashcode1 = field.hashCode();
        field = new UnsupportedDurationField(DurationFieldType.HOURS);
        int hashcode2 = field.hashCode();
        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals("UnsupportedDurationField[hours]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertSame(field, field.readResolve());
    }
}