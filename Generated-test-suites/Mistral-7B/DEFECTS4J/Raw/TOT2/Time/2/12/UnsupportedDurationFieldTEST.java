package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    @org.junit.Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField anotherField = UnsupportedDurationField.getInstance(type);

        assertEquals(type, field.getType());
        assertEquals(field, anotherField);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        String result = field.toString();

        String expected = "UnsupportedDurationField[MILLISECONDS]";
        assertEquals(expected, result);
    }

    @Test
    public void testConstructor() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);

        assertEquals(type, field.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        long duration = 1000;
        field.getValue(duration);
    }

    // Test other methods, adding test cases for edge cases and exceptions as necessary

    @org.junit.Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        Object obj = new Object();

        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, obj);
    }

    @org.junit.Test
    public void testHashcode() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);

        int hashcode1 = field1.hashCode();
        int hashcode2 = field2.hashCode();

        assertEquals(hashcode1, hashcode2);
    }

    // Test other methods, adding test cases for edge cases and exceptions as necessary

    @org.junit.Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.SECONDS);

        int comparisonResult = field1.compareTo(field2);
        int expectedComparisonResult = 0;
        assertEquals(expectedComparisonResult, comparisonResult);

        comparisonResult = field1.compareTo(field3);
        expectedComparisonResult = -1;
        assertEquals(expectedComparisonResult, comparisonResult);

        comparisonResult = field3.compareTo(field1);
        expectedComparisonResult = 1;
        assertEquals(expectedComparisonResult, comparisonResult);
    }

    @org.junit.Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);

        long unitMillis = field.getUnitMillis();
        long expectedUnitMillis = 0;
        assertEquals(expectedUnitMillis, unitMillis);
    }
}