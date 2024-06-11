package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("DAYS", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAt() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLongAt() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillisAt() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testaddLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1, 1);
    }

    @Test
    public void testgetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testcompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testequals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(field1.equals(field2));

        UnsupportedDurationField same = UnsupportedDurationField.getInstance(type1);
        assertTrue(field1.equals(same));
    }

    @Test
    public void testhashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), field.hashCode());
    }

    @Test
    public void testtoString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    @Test
    public void testreadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertSame(UnsupportedDurationField.getInstance(type), field.readResolve());
    }
}