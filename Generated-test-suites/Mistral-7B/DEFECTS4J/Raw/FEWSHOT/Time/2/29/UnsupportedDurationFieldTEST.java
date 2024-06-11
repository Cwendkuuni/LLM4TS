package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);

        unsupported.getValue(1);
        unsupported.getValueAsLong(1);
        unsupported.getValue(1, 1);
        unsupported.getValueAsLong(1, 1);
        unsupported.getMillis(1);
        unsupported.getMillis(1L);
        unsupported.getMillis(1, 1);
        unsupported.getMillis(1L, 1);
        unsupported.add(1, 1);
        unsupported.add(1, 1L);
        unsupported.add(1, 1, 1);
        unsupported.add(1, 1L, 1);
        unsupported.getValue(1, 1);
        unsupported.getValueAsLong(1, 1);
        unsupported.getDifference(1, 1);
        unsupported.getDifferenceAsLong(1, 1);
    }

    @Test
    public void testComparison() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);

        assertEquals(0, unsupported1.compareTo(unsupported1));
        assertEquals(0, unsupported1.compareTo(unsupported2));
        assertEquals(0, unsupported2.compareTo(unsupported1));
        assertEquals(0, unsupported2.compareTo(unsupported2));
    }

    @Test
    public void testEqualsAndHashcode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);

        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
    }
}