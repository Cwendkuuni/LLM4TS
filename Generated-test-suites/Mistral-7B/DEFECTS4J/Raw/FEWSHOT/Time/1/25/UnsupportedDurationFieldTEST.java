package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        assertEquals(field1, field2);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        assertEquals(DurationFieldType.instance("foo"), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        assertEquals("foo", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue(long duration) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getValue(1L, 123456789);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong(long duration, long instant) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getValueAsLong(123456789, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(long value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(int value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(int value, long instant) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getMillis(1, 123456789);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.add(123456789, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant, int value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.add(123456789, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant, long value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.add(123456789, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getDifference(123456789, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getDifferenceAsLong(123456789, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        field.getUnitMillis();
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo1"));
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo2"));
        assertNotEquals(field1, field2);
        assertEquals(1, field1.compareTo(field2));
        assertEquals(-1, field2.compareTo(field1));

        UnsupportedDurationField supportedField = new DurationField(1, DurationFieldType.SECONDS);
        assertEquals(1, field1.compareTo(supportedField));
        assertEquals(-1, supportedField.compareTo(field1));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo1"));
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo1"));
        assertEquals(field1, field2);

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo2"));
        assertNotEquals(field1, field3);

        DurationField supportedField = new DurationField(1, DurationFieldType.SECONDS);
        assertNotEquals(field1, supportedField);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo1"));
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo1"));
        assertEquals(field1.hashCode(), field2.hashCode());

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo2"));
        assertNotEquals(field1.hashCode(), field3.hashCode());

        DurationField supportedField = new DurationField(1, DurationFieldType.SECONDS);
        assertNotEquals(field1.hashCode(), supportedField.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.instance("foo"));
        assertEquals("UnsupportedDurationField[foo]", field.toString());
    }
}