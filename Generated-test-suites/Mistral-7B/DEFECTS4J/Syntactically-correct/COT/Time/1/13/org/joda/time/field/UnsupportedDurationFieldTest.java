package org.joda.time.field.test;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.isSupported();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getValue(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getValueAsLong(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getMillis(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getMillis(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getDifference(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        unsupported.getDifferenceAsLong(1L, 1L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.SECONDS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS());
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.SECONDS());
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.SECONDS());
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS());
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        assertEquals(unsupported.getName().hashCode(), unsupported.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.SECONDS());
        assertEquals("UnsupportedDurationField[SECONDS]", unsupported.toString());
    }
}