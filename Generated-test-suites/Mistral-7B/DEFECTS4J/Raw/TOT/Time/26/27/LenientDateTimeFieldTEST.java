package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private static final Chronology ISO_CHRONO = org.joda.time.chrono.ISOChronology.getInstanceUTC();
    private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();

    @Test
    public void testLenientDateTimeField() {
        DateTimeField strictField = new StrictDateTimeField(DAY_OF_MONTH, ISO_CHRONO);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, ISO_CHRONO);

        MutableDateTime dateTime = new MutableDateTime(1492, 1, 2, 0, 0, 0, ISO_CHRONO);

        // Test setting a valid value
        long originalValue = dateTime.get(DAY_OF_MONTH);
        long newValue = originalValue + 1;
        long setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, dateTime.getLong());
        assertEquals(newValue, dateTime.get(DAY_OF_MONTH));

        // Test setting a value out of bounds
        newValue += 31;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, dateTime.getLong() + 86400000L); // 1 day
        assertEquals(newValue, dateTime.get(DAY_OF_MONTH));

        // Test setting a negative value
        newValue = originalValue - 1;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, dateTime.getLong() - 86400000L); // 1 day
        assertEquals(newValue, dateTime.get(DAY_OF_MONTH));

        // Test setting a value beyond the maximum value for the field
        newValue = Integer.MAX_VALUE + 1;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, Long.MAX_VALUE);
        assertNotSame(newValue, dateTime.get(DAY_OF_MONTH));
        assertEquals(Integer.MAX_VALUE, dateTime.get(DAY_OF_MONTH));

        // Test setting a value beyond the minimum value for the field
        newValue = Integer.MIN_VALUE - 1;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, Long.MIN_VALUE);
        assertNotSame(newValue, dateTime.get(DAY_OF_MONTH));
        assertEquals(Integer.MIN_VALUE, dateTime.get(DAY_OF_MONTH));

        // Test setting a value that is not an integer
        newValue = 3.14F;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, dateTime.getLong());
        assertEquals(originalValue, dateTime.get(DAY_OF_MONTH));

        // Test setting a value that is NaN
        newValue = Float.NaN;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, dateTime.getLong());
        assertEquals(originalValue, dateTime.get(DAY_OF_MONTH));

        // Test setting a value that is Infinity
        newValue = Float.POSITIVE_INFINITY;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, Long.MAX_VALUE);
        assertNotSame(newValue, dateTime.get(DAY_OF_MONTH));
        assertEquals(Integer.MAX_VALUE, dateTime.get(DAY_of_MONTH));

        // Test setting a value that is -Infinity
        newValue = Float.NEGATIVE_INFINITY;
        setValue = lenientField.set(dateTime.getLong(), newValue);
        assertEquals(setValue, Long.MIN_VALUE);
        assertNotSame(newValue, dateTime.get(DAY_OF_MONTH));
        assertEquals(Integer.MIN_VALUE, dateTime.get(DAY_of_MONTH));
    }
}