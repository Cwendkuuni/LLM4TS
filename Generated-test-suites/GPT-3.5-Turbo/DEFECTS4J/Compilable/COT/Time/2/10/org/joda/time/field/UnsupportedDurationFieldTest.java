package org.joda.time.field;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnsupportedDurationFieldTest {

    @Test
    public void getType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(DurationFieldType.seconds(), field.getType());
    }

    @Test
    public void getName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("seconds", field.getName());
    }

    @Test
    public void isSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertFalse(field.isSupported());
    }

    @Test
    public void isPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValue(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueWithInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getValueAsLong(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getMillis(1L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.add(0, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifference(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        field.getDifferenceAsLong(0, 1);
    }

    @Test
    public void getUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void compareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(0, field1.compareTo(field2));
    }
}