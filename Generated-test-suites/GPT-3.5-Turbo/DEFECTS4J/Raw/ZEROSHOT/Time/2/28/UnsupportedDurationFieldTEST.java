package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(DurationFieldType.MILLIS, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("millis", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValue(1000, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getValueAsLong(1000, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(1000, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getMillis(1000L, 1234567890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInt() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.add(1234567890L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.add(1234567890L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getDifference(1234567890L, 987654321L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        field.getDifferenceAsLong(1234567890L, 987654321L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertSame(field1, field1.readResolve());
        assertSame(field2, field2.readResolve());
    }
}