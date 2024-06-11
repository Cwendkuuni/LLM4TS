package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(type);
        assertEquals(f1, f2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertEquals("days", f.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertFalse(f.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertTrue(f.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getMillis(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.add(0, 1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        f.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertEquals(0, f.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MILLIS;
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, f1.compareTo(f2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField f3 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(f1, f1);
        assertEquals(f1, f2);
        assertNotSame(f1, f3);
        assertNull(f1.toString());
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertEquals(f.hashCode(), f.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[days]", f.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(type);
        assertSame(f, f.readResolve());
    }

}