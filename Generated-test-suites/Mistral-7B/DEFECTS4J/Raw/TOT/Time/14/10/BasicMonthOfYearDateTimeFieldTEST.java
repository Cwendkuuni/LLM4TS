package org.joda.time.chrono.tests;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.ChronologyFactory;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

/**
 * Tests for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The chronology for testing. */
    private static final Chronology CHRONOLOGY = new BasicChronology("UTC", DateTimeZone.UTC);

    /** The field under test. */
    private static final BasicMonthOfYearDateTimeField MONTH_OF_YEAR_FIELD
        = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#get(long)} method.
     */
    @org.junit.Test
    public void testGet() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int i = 0; i < 12; i++) {
            int month = MONTH_OF_YEAR_FIELD.get(date.getMillis());
            // Test with known results
            assertEquals(i + 1, month);
            date.setMilliseconds(date.getMillis() + MONTH_OF_YEAR_FIELD.getMillisPerField());
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#add(long, int)} method.
     */
    @org.junit.Test
    public void testAdd() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int i = -12; i <= 12; i++) {
            long result = MONTH_OF_YEAR_FIELD.add(date.getMillis(), i);
            int month = MONTH_OF_YEAR_FIELD.get(result);
            // Test with known results
            assertEquals(i + 1, month);
            date.setMilliseconds(result);
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#set(long, int)} method.
     */
    @org.junit.Test
    public void testSet() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int i = 1; i <= 12; i++) {
            long result = MONTH_OF_YEAR_FIELD.set(date.getMillis(), i);
            int month = MONTH_OF_YEAR_FIELD.get(result);
            // Test with known results
            assertEquals(i, month);
            assertEquals(result, date.getMillis());
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#isLeap(long)} method.
     */
    @org.junit.Test
    public void testIsLeap() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int year = DateTimeConstants.MIN_YEAR; year <= DateTimeConstants.MAX_YEAR; year++) {
            boolean leap = MONTH_OF_YEAR_FIELD.isLeap(date.getMillis(year, DateTimeConstants.JANUARY));
            boolean expected = CHRONOLOGY.isLeapYear(year);
            // Test with known results
            assertEquals(expected, leap);
            date.setYear(year);
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#getLeapAmount(long)} method.
     */
    @org.junit.Test
    public void testGetLeapAmount() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int year = DateTimeConstants.MIN_YEAR; year <= DateTimeConstants.MAX_YEAR; year++) {
            int leapAmount = MONTH_OF_YEAR_FIELD.getLeapAmount(date.getMillis(year, DateTimeConstants.JANUARY));
            int expected = CHRONOLOGY.isLeapYear(year) ? 1 : 0;
            // Test with known results
            assertEquals(expected, leapAmount);
            date.setYear(year);
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#addWrapField(long, int)} method.
     */
    @org.junit.Test
    public void testAddWrapField() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int i = -12; i <= 12; i++) {
            long result = MONTH_OF_YEAR_FIELD.addWrapField(date.getMillis(), i);
            int month = MONTH_OF_YEAR_FIELD.get(result);
            // Test with known results
            assertEquals(FieldUtils.getWrappedValue(MONTH_OF_YEAR_FIELD.get(date.getMillis()), i, 1, 12), month);
            assertEquals(result, date.getMillis());
            date.setMilliseconds(result);
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#getDifferenceAsLong(long, long)} method.
     */
    @org.junit.Test
    public void testGetDifferenceAsLong() {
        MutableDateTime date1 = new MutableDateTime(0L);
        MutableDateTime date2 = new MutableDateTime(0L);
        long difference = MONTH_OF_YEAR_FIELD.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        int expected = MONTH_OF_YEAR_FIELD.get(date1.getMillis()) - MONTH_OF_YEAR_FIELD.get(date2.getMillis());
        // Test with known results
        assertEquals(expected, difference);
        // Test edge cases
        assertEquals(-expected, MONTH_OF_YEAR_FIELD.getDifferenceAsLong(date2.getMillis(), date1.getMillis()));
        assertEquals(-expected, MONTH_OF_YEAR_FIELD.getDifferenceAsLong(date1.getMillis(), date1.getMillis()));
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#getRangeDurationField()} method.
     */
    @org.junit.Test
    public void testGetRangeDurationField() {
        DurationField yearRange = MONTH_OF_YEAR_FIELD.getRangeDurationField();
        assertEquals(DurationFieldType.years(), yearRange.getType());
        assertEquals(1, yearRange.getValues()[0]);
        assertEquals(1, yearRange.getValues()[1]);
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#roundFloor(long)} method.
     */
    @org.junit.Test
    public void testRoundFloor() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int i = 0; i < 12; i++) {
            long result = MONTH_OF_YEAR_FIELD.roundFloor(date.getMillis());
            int month = MONTH_OF_YEAR_FIELD.get(result);
            assertEquals(i, month);
            date.setMilliseconds(result);
        }
    }

    /**
     * Tests the {@link BasicMonthOfYearDateTimeField#remainder(long)} method.
     */
    @org.junit.Test
    public void testRemainder() {
        MutableDateTime date = new MutableDateTime(0L);
        for (int i = 0; i < 12; i++) {
            long result = MONTH_OF_YEAR_FIELD.remainder(date.getMillis());
            int expected = (int) (MONTH_OF_YEAR_FIELD.get(date.getMillis()) % 1);
            assertEquals(expected, result);
            date.setMilliseconds(date.getMillis() + MONTH_OF_YEAR_FIELD.getMillisPerField());
        }
    }
}