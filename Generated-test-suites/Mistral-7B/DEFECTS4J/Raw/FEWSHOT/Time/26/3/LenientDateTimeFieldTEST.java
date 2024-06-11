package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientGetInstance() {
        DateTimeField field = mock(DateTimeField.class);
        Chronology base = mock(Chronology.class);
        when(field.isLenient()).thenReturn(false);

        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField);

        when(field.isLenient()).thenReturn(true);

        lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField);

        // Test null inputs
        lenientField = LenientDateTimeField.getInstance(null, base);
        assertNull(lenientField);
        lenientField = LenientDateTimeField.getInstance(field, null);
        assertNull(lenientField);
    }

    @Test
    public void testLenientSet() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(type, 30, base);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        long newValue = 31;

        long expectedLocalInstant = instant + (24 * 60 * 60 * 1000L); // One day in milliseconds
        long expectedUTCInstant = ZoneUtils.toUTC(expectedLocalInstant);

        long result = lenientField.set(instant, newValue);

        assertEquals(expectedUTCInstant, result);

        // Test negative values
        newValue = -1;
        expectedLocalInstant -= (24 * 60 * 60 * 1000L); // One day in milliseconds
        expectedUTCInstant -= (24 * 60 * 60 * 1000L * 2); // Two days in milliseconds

        result = lenientField.set(instant, newValue);

        assertEquals(expectedUTCInstant, result);

        // Test zero value
        newValue = 0;
        expectedLocalInstant = instant;
        expectedUTCInstant = instant;

        result = lenientField.set(instant, newValue);

        assertEquals(expectedUTCInstant, result);

        // Test maximum value
        newValue = LenientDateTimeField.getMaximumValue(type, base);
        expectedLocalInstant += (24 * 60 * 60 * 1000L); // One day in milliseconds
        expectedUTCInstant += (24 * 60 * 60 * 1000L * 2); // Two days in milliseconds

        result = lenientField.set(instant, newValue);

        assertEquals(expectedUTCInstant, result);

        // Test maximum value with negative time zone
        base = mock(org.joda.time.chrono.BaseChronology.class);
        when(base.getRules().getOffset(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthOfYear(),
                LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getDayOfWeek()))
                .thenReturn(-(24 * 60 * 60 * 1000L));
        field = new StrictDateTimeField(type, 30, base);
        lenientField = new LenientDateTimeField(field, base);

        result = lenientField.set(instant, newValue);

        assertEquals(expectedUTCInstant + (24 * 60 * 60 * 1000L * 3), result); // Three days in milliseconds
    }
}