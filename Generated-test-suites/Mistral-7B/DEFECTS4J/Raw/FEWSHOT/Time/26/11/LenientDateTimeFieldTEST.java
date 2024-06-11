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
    public void testLenientDateTimeField() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(type, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        assertEquals(field, lenientField); // same underlying field
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSetLenientDateTimeField() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(type, base);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = LocalDateTime.parse("2023-01-01T00:00:00.000Z").toInstant().getEpochMilli();
        int value = 31; // out of bounds for Jan 2023

        when(base.getZone()).thenReturn(ZoneUtils.UTC);
        long expected = instant + (31 - getDayOfMonth(instant));

        long actual = lenientField.set(instant, value);
        assertEquals(expected, actual);
    }

    private int getDayOfMonth(long instant) {
        LocalDateTime localDateTime = new LocalDateTime(instant).withZoneRetainFields(ZoneUtils.UTC);
        return localDateTime.getDayOfMonth();
    }
}