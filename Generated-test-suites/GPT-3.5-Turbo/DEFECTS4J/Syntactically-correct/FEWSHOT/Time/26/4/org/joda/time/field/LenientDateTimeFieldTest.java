
package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private DateTimeField field;
    private Chronology base;

    @Before
    public void setUp() {
        field = new LenientDateTimeField(new BaseDateTimeField(), base);
    }

    @Test
    public void testGetInstance() {
        DateTimeField strictField = new StrictDateTimeField(new BaseDateTimeField());
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        assertTrue(lenientField instanceof LenientDateTimeField);
        assertEquals(strictField, ((LenientDateTimeField) lenientField).getWrappedField());

        DateTimeField lenientField2 = LenientDateTimeField.getInstance(lenientField, base);
        assertEquals(lenientField, lenientField2);
    }

    @Test
    public void testIsLenient() {
        assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1000L;
        int value = 5;

        long expectedInstant = base.withUTC(instant).plus(value - field.get(instant));
        long result = field.set(instant, value);

        assertEquals(expectedInstant, result);
    }
}
