package org.joda.time.field;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[DAYS]";
        String actual = field.toString();
        assertSame(expected, actual);
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(field1.equals(field2));

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type1);
        assertTrue(field1.equals(field3));

        Object obj = new Object();
        assertFalse(field1.equals(obj));

        UnsupportedDurationField field4 = new UnsupportedDurationField(null);
        assertFalse(field1.equals(field4));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        int hashCode1 = field1.hashCode();

        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        int hashCode2 = field2.hashCode();

        assertEquals(hashCode1, hashCode2);

        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type2);
        int hashCode3 = field3.hashCode();

        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);

        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);

        DurationFieldType type3 = DurationFieldType.HOURS;
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type3);

        int comparison = field1.compareTo(field2);
        assertEquals(1, comparison);

        int comparison2 = field1.compareTo(field3);
        assertEquals(0, comparison2);

        int comparison3 = field2.compareTo(field3);
        assertEquals(-1, comparison3);
    }
}