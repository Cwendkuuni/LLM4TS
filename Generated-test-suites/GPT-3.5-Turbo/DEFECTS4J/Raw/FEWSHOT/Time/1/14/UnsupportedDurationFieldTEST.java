package org.joda.time.field;

import org.joda.time.DurationFieldType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        assertEquals(DurationFieldType.ERA_TYPE, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        assertEquals(DurationFieldType.ERA_TYPE.getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValue(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValueAsLong(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValue(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getValueAsLong(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(100, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getMillis(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstantValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.add(100L, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstantValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.add(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getDifference(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        field.getDifferenceAsLong(100L, 200L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.ERA_TYPE);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HALFDAY_TYPE);
        assertTrue(field1.compareTo(field2) < 0);
        assertTrue(field2.compareTo(field1) > 0);
        assertTrue(field1.compareTo(field1) == 0);
    }
}