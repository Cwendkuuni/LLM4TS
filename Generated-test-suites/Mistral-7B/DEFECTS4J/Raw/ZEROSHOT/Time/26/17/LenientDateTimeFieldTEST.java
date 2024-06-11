package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.Period;
import org.joda.time.ZoneInfo;
import org.joda.time.base.BaseCalendar;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.LenientDateTimeFieldType;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.field.StrictDateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * Test file for LenientDateTimeField class.
 */
public class LenientDateTimeFieldTest {

    /**
     * Test method for {@link LenientDateTimeField#getInstance(DateTimeField, Chronology)}
     */
    @Test
    public void testGetInstance() {
        Chronology chrono = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.MINUTE_OF_HOUR, 1, chrono);

        LenientDateTimeField lenientField1 = LenientDateTimeField.getInstance(field, chrono);
        assertEquals(field, lenientField1.getBase());
        assertEquals(LenientDateTimeFieldType.MINUTE_OF_HOUR, lenientField1.getType());
        assertTrue(lenientField1.isLenient());

        LenientDateTimeField lenientField2 = new LenientDateTimeField(field, chrono);
        assertEquals(field, lenientField2.getBase());
        assertEquals(LenientDateTimeFieldType.MINUTE_OF_HOUR, lenientField2.getType());
        assertTrue(lenientField2.isLenient());

        assertNotSame(lenientField1, lenientField2);

        DateTimeField lenientField3 = lenientField1;
        assertEquals(lenientField1, lenientField3);
        assertEquals(field, lenientField3.getBase());
        assertEquals(LenientDateTimeFieldType.MINUTE_OF_HOUR, lenientField3.getType());
        assertTrue(lenientField3.isLenient());
    }

    /**
     * Test method for {@link LenientDateTimeField#set(long, int)}
     */
    @Test
    public void testSet() {
        Chronology chrono = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.MINUTE_OF_HOUR, 1, chrono);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, 0, chrono);
        LocalDateTime expected = new LocalDateTime(0, 1, 1, 1, 1, 1, 0, 0, chrono);

        LenientDateTimeField lenientField = new LenientDateTimeField(field, chrono);

        // Set valid value
        long currentValue = lenientField.get(dateTime.toLocalMillis());
        lenientField.set(dateTime.toLocalMillis(), currentValue);
        assertEquals(currentValue, lenientField.get(dateTime.toLocalMillis()));

        // Set a value greater than the maximum
        lenientField.set(dateTime.toLocalMillis(), Long.MAX_VALUE + 1);
        assertEquals(Long.MAX_VALUE + 1 + lenientField.get(dateTime.toLocalMillis()),
                lenientField.get(dateTime.toLocalMillis()));
        LocalDateTime result = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expected.plus(new Period(0, 1, 0)).toLocalDateTime(), result);

        // Set a value smaller than the minimum
        lenientField.set(dateTime.toLocalMillis(), Long.MIN_VALUE - 1);
        assertEquals(Long.MIN_VALUE - 1 + lenientField.get(dateTime.toLocalMillis()),
                lenientField.get(dateTime.toLocalMillis()));
        result = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expected.minus(new Period(0, 1, 0)).toLocalDateTime(), result);
    }

    /**
     * Test method for {@link LenientDateTimeField#set(long, int)} with different
     * chronologies
     */
    @Test
    public void testSetWithDifferentChronologies() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.MINUTE_OF_HOUR, 1, null);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, 0, BaseCalendar.UTC);
        LocalDateTime expectedUTC = new LocalDateTime(0, 1, 1, 1, 1, 1, 0, 0, BaseCalendar.UTC);
        LocalDateTime expectedAustralia = new LocalDateTime(0, 1, 1, 13, 1, 1, 0, 0, ZoneInfo. australia/sydney );

        Chronology chronoUTC = BaseCalendar.UTC;
        Chronology chronoAustralia = ZoneInfo. australia/sydney;

        LenientDateTimeField lenientFieldUTC = new LenientDateTimeField(field, chronoUTC);
        LenientDateTimeField lenientFieldAustralia = new LenientDateTimeField(field, chronoAustralia);

        // Set valid value
        long currentValueUTC = lenientFieldUTC.get(dateTime.toLocalMillis());
        lenientFieldUTC.set(dateTime.toLocalMillis(), currentValueUTC);
        assertEquals(currentValueUTC, lenientFieldUTC.get(dateTime.toLocalMillis()));

        lenientFieldUTC.set(dateTime.toLocalMillis(), Long.MAX_VALUE + 1);
        assertEquals(Long.MAX_VALUE + 1 + lenientFieldUTC.get(dateTime.toLocalMillis()),
                lenientFieldUTC.get(dateTime.toLocalMillis()));
        LocalDateTime resultUTC = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expectedUTC.plus(new Period(0, 1, 0)).toLocalDateTime(), resultUTC);

        lenientFieldUTC.set(dateTime.toLocalMillis(), Long.MIN_VALUE - 1);
        assertEquals(Long.MIN_VALUE - 1 + lenientFieldUTC.get(dateTime.toLocalMillis()),
                lenientFieldUTC.get(dateTime.toLocalMillis()));
        resultUTC = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expectedUTC.minus(new Period(0, 1, 0)).toLocalDateTime(), resultUTC);

        // Set valid value with different chronology
        long currentValueAustralia = lenientFieldAustralia.get(dateTime.toLocalMillis());
        lenientFieldAustralia.set(dateTime.toLocalMillis(), currentValueAustralia);
        assertEquals(currentValueAustralia, lenientFieldAustralia.get(dateTime.toLocalMillis()));

        lenientFieldAustralia.set(dateTime.toLocalMillis(), Long.MAX_VALUE + 1);
        assertEquals(Long.MAX_VALUE + 1 + lenientFieldAustralia.get(dateTime.toLocalMillis()),
                lenientFieldAustralia.get(dateTime.toLocalMillis()));
        resultAustralia = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expectedAustralia.plus(new Period(0, 12, 0)).toLocalDateTime(), resultAustralia);

        lenientFieldAustralia.set(dateTime.toLocalMillis(), Long.MIN_VALUE - 1);
        assertEquals(Long.MIN_VALUE - 1 + lenientFieldAustralia.get(dateTime.toLocalMillis()),
                lenientFieldAustralia.get(dateTime.toLocalMillis()));
        resultAustralia = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expectedAustralia.minus(new Period(0, 12, 0)).toLocalDateTime(), resultAustralia);
    }

    /**
     * Test method for {@link LenientDateTimeField#set(long, int)} with different field types
     */
    @Test
    public void testSetWithDifferentFieldTypes() {
        Chronology chrono = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.MINUTE_OF_HOUR, 1, chrono);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, 0, chrono);
        LocalDateTime expected = new LocalDateTime(0, 1, 1, 1, 1, 1, 0, 0, chrono);

        LenientDateTimeField lenientField = new LenientDateTimeField(field, chrono);

        // Test setting minute of hour with minute of hour field
        long currentValue = lenientField.get(dateTime.toLocalMillis());
        lenientField.set(dateTime.toLocalMillis(), currentValue);
        assertEquals(currentValue, lenientField.get(dateTime.toLocalMillis()));

        lenientField.set(dateTime.toLocalMillis(), Long.MAX_VALUE + 1);
        assertEquals(Long.MAX_VALUE + 1 + lenientField.get(dateTime.toLocalMillis()),
                lenientField.get(dateTime.toLocalMillis()));
        LocalDateTime result = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expected.plus(new Period(0, 1, 0)).toLocalDateTime(), result);

        lenientField.set(dateTime.toLocalMillis(), Long.MIN_VALUE - 1);
        assertEquals(Long.MIN_VALUE - 1 + lenientField.get(dateTime.toLocalMillis()),
                lenientField.get(dateTime.toLocalMillis()));
        result = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getSecondOfMinute(), dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expected.minus(new Period(0, 1, 0)).toLocalDateTime(), result);

        // Test setting minute of hour with second of minute field
        DateTimeField secondOfMinuteField = new StrictDateTimeField(DateTimeFieldType.SECOND_OF_MINUTE, 1, chrono);
        LenientDateTimeField lenientSecondField = new LenientDateTimeField(secondOfMinuteField, chrono);
        lenientSecondField.set(dateTime.toLocalMillis(), 60);
        lenientSecondField.set(dateTime.toLocalMillis(), 61);
        assertEquals(61, lenientSecondField.get(dateTime.toLocalMillis()));
        result = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour() + 1, 1,
                dateTime.getMillisecondOfSecond(), dateTime.getMillisecond(), dateTime.getZone());
        assertEquals(expected.plus(new Period(0, 1, 0)).toLocalDateTime(), result);

        lenientSecondField.set(dateTime.toLocalMillis(), -1);
        assertEquals(-1, lenientSecondField.get(dateTime.toLocalMillis()));
        result = new LocalDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(),
                dateTime.getMinuteOfHour() - 1, dateTime.getSecondOfMinute(), dateTime.getMillisecond(),
                dateTime.getZone());
        assertEquals(expected.minus(new Period(0, 1, 0)).toLocalDateTime(), result);
    }

    /**
     * Test method for {@link LenientDateTimeField#set(long, int)} with DateTimeFormatter
     */
    @Test
    public void testSetWithDateTimeFormatter() {
        Chronology chrono = mock(Chronology.class);
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.MINUTE_OF_HOUR, 1, chrono);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

        LenientDateTimeField lenientField = new LenientDateTimeField(field, chrono);

        long instant = LocalDateTime.parse("01/01/2000 01:01", formatter).toInstant().getMillis();
        int value = 1;
        lenientField.set(instant, value);
        assertEquals(value, lenientField.get(instant));

        value = 60;
        lenientField.set(instant, value);
        assertEquals(value, lenientField.get(instant));
        LocalDateTime expected = new LocalDateTime(2000, 1, 1, 1, 1 + value, 0, 0, 0, chrono);
        LocalDateTime result = new LocalDateTime(instant, chrono);
        assertEquals(expected.toLocalDateTime(), result);

        value = -1;
        lenientField.set(instant, value);
        assertEquals(value, lenientField.get(instant));
        result = new LocalDateTime(instant, chrono);
        expected = expected.minus(new Period(0, 1, 0));
        assertEquals(expected.toLocalDateTime(), result);
    }
}