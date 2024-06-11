package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFields;
import org.joda.time.Fields;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology CHRONOLOGY = new BasicGregorianChronology();
    private static final BasicMonthOfYearDateTimeField FIELD = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, FIELD.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, FIELD.getMaximumValue());
    }

    @Test
    public void testGet() {
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(0L));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31 * 30));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 10));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY * 364));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 365 * 50 + DateTimeConstants.MILLIS_PER_DAY * 29));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 365 * 10000));
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31564000));

        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31 * 31 + 1));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31 * 31 * 2 + 2));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 2 + 1));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 1000 + 1));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 1000 * 100 + 1));

        assertEquals(DateTimeConstants.MARCH, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31 * 31 * 31 + 1));
        assertEquals(DateTimeConstants.MARCH, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 1000 * 1000 * 100 + 1));

        // Check for leap year
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 1000 * 100 * 4 + 1));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 1000 * 100 * 400 + 1));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(DateTimeConstants.MILLIS_PER_DAY * 31536000 * 1000 * 100 * 400000 + 1));
    }

    @Test
    public void testAdd() {
        long instant = 0;

        // Test adding 1 month
        instant = FIELD.add(instant, 1);
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(instant));

        // Test adding 11 months
        instant = FIELD.add(instant, 11);
        assertEquals(DateTimeConstants.NOVEMBER, FIELD.get(instant));

        // Test adding a negative number of months
        instant = FIELD.add(instant, -1);
        assertEquals(DateTimeConstants.OCTOBER, FIELD.get(instant));

        // Test adding 12 months multiple times
        for (int i = 0; i < 10; i++) {
            instant = FIELD.add(instant, 12);
        }
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));

        // Test adding a large number of months
        instant = FIELD.add(instant, 7000);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));
    }

    @Test
    public void testSet() {
        long instant = 0;

        // Test setting to valid values
        instant = FIELD.set(instant, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));

        instant = FIELD.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(instant));

        instant = FIELD.set(instant, DateTimeConstants.MARCH);
        assertEquals(DateTimeConstants.MARCH, FIELD.get(instant));

        // Test setting to invalid value
        instant = FIELD.set(instant, DateTimeConstants.JANUARY + 1);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));
    }

    @Test
    public void testIsLeap() {
        assertFalse(FIELD.isLeap(0L));
        assertFalse(FIELD.isLeap(-1L));

        // Test leap year
        LocalDateTime leapDate = new LocalDateTime(2000, 2, 29, 0, 0, 0);
        long leapInstant = CHRONOLOGY.getMillis(leapDate);
        assertTrue(FIELD.isLeap(leapInstant));
    }

    @Test
    public void testGetLeapAmount() {
        assertNull(FIELD.getLeapAmount(0L));
        assertNull(FIELD.getLeapAmount(-1L));

        // Test leap year
        LocalDateTime leapDate = new LocalDateTime(2000, 2, 29, 0, 0, 0);
        long leapInstant = CHRONOLOGY.getMillis(leapDate);
        assertEquals(1, FIELD.getLeapAmount(leapInstant));
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(DurationFields.DAYS, FIELD.getLeapDurationField());
    }

    @Test
    public void testAddWrapField() {
        long instant = 0;

        // Test wrapping around to previous month
        instant = FIELD.addWrapField(instant, 12);
        assertEquals(DateTimeConstants.DECEMBER, FIELD.get(instant));
        assertEquals(DateTimeConstants.DECEMBER, FIELD.get(instant - DateTimeConstants.MILLIS_PER_DAY));

        // Test wrapping around to next month
        instant = FIELD.addWrapField(instant, -13);
        assertEquals(DateTimeConstants.NOVEMBER, FIELD.get(instant));
        assertEquals(DateTimeConstants.NOVEMBER, FIELD.get(instant + DateTimeConstants.MILLIS_PER_DAY));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = 0;
        long instant2 = 0;

        // Test zero difference
        assertEquals(0, FIELD.getDifferenceAsLong(instant1, instant1));

        // Test negative difference
        instant2 = FIELD.add(instant1, -1);
        assertEquals(-1, FIELD.getDifferenceAsLong(instant1, instant2));

        // Test positive difference
        instant2 = FIELD.add(instant1, 1);
        assertEquals(1, FIELD.getDifferenceAsLong(instant1, instant2));

        // Test difference across year boundary
        instant1 = DateTimeConstants.MILLIS_PER_DAY * 365;
        instant2 = DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(1, FIELD.getDifferenceAsLong(instant1, instant2));

        // Test difference between two leap years
        instant1 = DateTimeConstants.MILLIS_PER_DAY * 365 * 4;
        instant2 = DateTimeConstants.MILLIS_PER_DAY * 365 * 4 + DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(1, FIELD.getDifferenceAsLong(instant1, instant2));

        // Test difference between January 1 and December 31
        long start = DateTimeConstants.MILLIS_PER_DAY * 31;
        long end = DateTimeConstants.MILLIS_PER_DAY * 365;
        long daysInYear = end - start;
        assertEquals(daysInYear, FIELD.getDifferenceAsLong(start, end));
    }

    @Test
    public void testAddReadablePartial() {
        ReadablePartial partial = new LocalDate();

        // Test adding month to partial
        partial = FIELD.add(partial, 1, FIELD, 1);
        assertEquals(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate("2000-02-29").toLocalDate(), partial.toLocalDate());

        // Test adding month to partial with wrap-around
        partial = new LocalDate(2000, 11, 31);
        partial = FIELD.add(partial, 1, FIELD, 1);
        assertEquals(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate("1999-12-31").toLocalDate(), partial.toLocalDate());

        // Test adding months to partial
        partial = new LocalDate(2000, 1, 1);
        partial = FIELD.add(partial, 12, FIELD, -1);
        assertEquals(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate("1999-12-31").toLocalDate(), partial.toLocalDate());

        // Test adding months to partial with wrap-around and different field types
        LocalDateTime dateTime = new LocalDateTime(2000, 1, 1, 12, 0, 0);
        partial = FIELD.add(dateTime.toLocalDate(), 12, FIELD, -1);
        assertEquals(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").parseLocalDateTime("1999-12-31 12:00:00.000").toLocalDateTime(), partial.toLocalDateTime());

        // Test adding months to partial with different field types and add amount larger than 12 months
        partial = new LocalDateTime(2000, 1, 1, 12, 0, 0).toLocalDate();
        partial = FIELD.add(partial, 24, FIELD, -12);
        assertEquals(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate("1999-12-31").toLocalDate(), partial.toLocalDate());
    }
}