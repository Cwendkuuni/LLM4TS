package org.joda.time.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getValue(12345L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(12345L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.getValue(12345L, 67890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValueAsLong(12345L, 67890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(12345);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        field.getMillis(12345L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        field.getMillis(12345, 67890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getMillis(12345L, 67890L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(12345L, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        field.add(12345L, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        field.getDifference(123456789L, 987654321L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifferenceAsLong(123456789L, 987654321L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(1, field1.compareTo(field3));
        assertTrue(field1.compareTo(field2) >= 0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(field1, field2);
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[" + field.getName() + "]", field.toString());
    }
}