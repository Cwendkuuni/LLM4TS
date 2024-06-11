package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("Minutes", field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertFalse(field.isSupported());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[Minutes]";
        assertEquals(expected, field.toString());
    }

    @Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = (UnsupportedDurationField) SerializationUtils.serialize(field1);
        assertEquals(field1, field2);
    }
}