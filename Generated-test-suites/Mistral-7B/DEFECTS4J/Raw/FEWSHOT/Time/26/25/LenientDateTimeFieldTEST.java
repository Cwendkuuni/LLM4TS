package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.ZoneId;

import java.time.ZonedDateTime;

public class LenientDateTimeFieldTest {

    private static final Chronology UTC = Chronology.UTC;
    private static final Chronology DATE_TIME = Chronology.getInstance("Europe/London");

    @Test
    public void testLenientGetInstance() {
        DateTimeField field = DateTimeFields.minutes();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);
        assertSame(field, lenientField);

        field = new LocalDateTimeField(DateTimeFields.minuteOfHour(), DateTimeFields.minuteOfHour(), DateTimeFields.hourOfDay(), UTC);
        lenientField = LenientDateTimeField.getInstance(field, DATE_TIME);
        assertEquals(field.getClass(), lenientField.getClass());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientSet() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        Chronology base = UTC;

        long utcInstant = 1633040000000L; // 2012-01-01 00:01:00.000 UTC
        LocalDateTime localDateTime = new LocalDateTime(base, 2012, 1, 1, 1, 1).withZoneRetainFields(ZoneId.of("Europe/London"));
        long localInstant = localDateTime.getMillis();

        int value = 150; // 2 hours and 30 minutes after 1:01:00
        long expectedLocalUTCToSet = localInstant + (value * 60 * 60 * 1000);
        long expectedLocalToSet = localDateTime.withFieldAdded(DateTimeFields.minuteOfHour(), value).getMillis();

        LenientDateTimeField lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, base);
        long actualLocalUTCToSet = lenientField.set(localInstant, value);
        assertEquals(expectedLocalUTCToSet, actualLocalUTCToSet);

        long actualLocalToSet = new LocalDateTime(base, 2012, 1, 1, 1, value).getMillis();
        assertEquals(expectedLocalToSet, actualLocalToSet);

        long actualUtcToSet = base.getZone().convertLocalToUTC(actualLocalToSet, false);
        assertEquals(expectedLocalUTCToSet, actualUtcToSet);

        ZonedDateTime expectedZonedDateTime = ZonedDateTime.ofInstant(JavaTimeConverters.toInstant(expectedLocalToSet), base.getZone());
        ZonedDateTime actualZonedDateTime = new ZonedDateTime(JavaTimeConverters.toInstant(actualUtcToSet), base.getZone());
        assertEquals(expectedZonedDateTime, actualZonedDateTime);

        int newValue = 200; // 3 hours and 20 minutes after 1:01:00
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, base);
        long actualLocalUTCToSetNewValue = lenientField.set(actualUtcToSet, newValue);
        long expectedLocalUTCToSetNewValue = expectedLocalUTCToSet + (newValue - value) * 60 * 60 * 1000;
        assertEquals(expectedLocalUTCToSetNewValue, actualLocalUTCToSetNewValue);
    }

    @Test
    public void testLenientSetNull() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        Chronology base = UTC;

        LenientDateTimeField lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, base);
        assertNull(lenientField.set(Long.MIN_VALUE, Integer.MIN_VALUE));
        assertNull(lenientField.set(Long.MAX_VALUE, Integer.MAX_VALUE));
        assertNull(lenientField.set(null, Integer.MIN_VALUE));
        assertNull(lenientField.set(null, Integer.MAX_VALUE));
        assertNull(lenientField.set(null, null));
    }

    @Test
    public void testLenientIsLenient() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        Chronology base = UTC;

        LenientDateTimeField lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, base);
        assertTrue(lenientField.isLenient());

        DateTimeField strictField = (DateTimeField) DateTimeFields.minuteOfHour();
        assertFalse(strictField.isLenient());
    }
}