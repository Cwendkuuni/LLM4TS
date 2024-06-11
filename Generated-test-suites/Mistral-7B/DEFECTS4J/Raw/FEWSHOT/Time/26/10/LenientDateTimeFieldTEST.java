package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneUtils;
import org.joda.time.field.BaseDateTimeFieldTest;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link LenientDateTimeField}.
 *
 * @author Alexander Tymofeyev
 */
public class LenientDateTimeFieldTest extends BaseDateTimeFieldTest {

    private Chronology chronology;
    private DateTimeField field;
    private DateTimeFormatter formatter;

    @Before
    public void setUp() {
        chronology = mock(Chronology.class);
        field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_WEEK, 2, null);
        when(chronology.getUTC().getField(DateTimeFieldType.DAY_OF_WEEK)).thenReturn(field);
        when(chronology.getZone()).thenReturn(ZoneUtils.UTC);
        formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Test
    public void testLenientVersion() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        assertSame(field, lenientField.getWrappedField());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSetValue() {
        MutableDateTime mutableDateTime = new MutableDateTime();
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime();

        LocalDate date = LocalDate.parse("2018-12-31", formatter);
        LocalDateTime time = LocalDateTime.parse("2018-12-31T23:59:59.999", formatter);
        mutableDateTime.set(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getHourOfDay(),
                time.getMinuteOfHour(), time.getSecondOfMinute(), 0, 0, 0, 0);
        long instant = mutableDateTime.getMillis();

        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        int value = get(instant) + 1;
        long result = lenientField.set(instant, value);

        assertEquals(instant + FieldUtils.ONE_DAY, result, 0L);
    }

    @Test
    public void testSetValueOutOfBound() {
        MutableDateTime mutableDateTime = new MutableDateTime();
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime();

        LocalDate date = LocalDate.parse("2018-12-31", formatter);
        LocalDateTime time = LocalDateTime.parse("2018-12-31T23:59:59.999", formatter);
        mutableDateTime.set(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getHourOfDay(),
                time.getMinuteOfHour(), time.getSecondOfMinute(), 0, 0, 0, 0);
        long instant = mutableDateTime.getMillis();

        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        int value = get(instant) - FieldUtils.ONE_DAY;
        long result = lenientField.set(instant, value);

        assertEquals(instant - FieldUtils.ONE_DAY, result, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueException() {
        MutableDateTime mutableDateTime = new MutableDateTime();
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime();

        LocalDate date = LocalDate.parse("2018-12-31", formatter);
        LocalDateTime time = LocalDateTime.parse("2018-12-31T23:59:59.999", formatter);
        mutableDateTime.set(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getHourOfDay(),
                time.getMinuteOfHour(), time.getSecondOfMinute(), 0, 0, 0, 0);
        long instant = mutableDateTime.getMillis();

        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        lenientField.set(instant, Integer.MIN_VALUE);
    }

    @Test
    public void testAdd() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.YEAR, 2000, null);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        int value = 1;

        long result = lenientField.add(0, value);
        assertEquals(2001, get(result));

        result = lenientField.add(2000, value);
        assertEquals(2002, get(result));

        result = lenientField.add(Long.MAX_VALUE, value);
        assertEquals(2002, get(result));
    }

    @Test
    public void testGet() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH, 31, null);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        long instant = LocalDateTime.parse("2018-12-31T23:59:59.999", formatter).toInstant().getEpochMilli();

        int result = lenientField.get(instant);
        assertEquals(31, result);

        result = lenientField.get(instant + FieldUtils.ONE_DAY.getMillis());
        assertEquals(1, result);
    }
}