package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getValue(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getValueAsLong(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getValueAsLong(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        assertEquals(DurationFieldType.INSTANT_SECONDS.getName(), field.getName());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        int hashcode1 = field1.hashCode();
        int hashcode2 = field2.hashCode();
        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT_SECONDS);
        String toString = field.toString();
        String expected = "UnsupportedDurationField[INSTANT_SECONDS]";
        assertEquals(expected, toString);
    }

    @Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        Object output = new ObjectOutputStream(new ByteArrayOutputStream())
                .writeObject(field1);
        UnsupportedDurationField field2 = (UnsupportedDurationField) new ObjectInputStream(new ByteArrayInputStream(
                ((ByteArrayOutputStream) output).toByteArray()))
                .readObject();
        assertEquals(field1, field2);
    }
}