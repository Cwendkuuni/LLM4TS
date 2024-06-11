
package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(DurationFieldType.months(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("months", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValueAsLong(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValue(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getValueAsLong(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getMillis(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getMillis(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.add(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.add(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getDifference(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        field.getDifferenceAsLong(100L, 200L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals("UnsupportedDurationField[months]", field.toString());
    }
}
