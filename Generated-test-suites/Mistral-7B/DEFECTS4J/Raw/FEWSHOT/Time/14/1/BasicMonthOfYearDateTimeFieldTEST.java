package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.JulianFields;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology CHRONOLOGY = new BasicGregorianChronology();
    private static final BasicMonthOfYearDateTimeField TEST_FIELD =
            new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    @Test
    public void testIsLenient() {
        assertFalse(TEST_FIELD.isLenient());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInvalidInstant() {
        TEST_FIELD.get(Long.MIN_VALUE);
    }

    @Test
    public void testGetValidInstant() {
        int month = TEST_FIELD.get(ImmutableDateTime.now().getMillis());
        assertEquals(TEST_FIELD.getFieldType(), DateTimeFieldType.monthOfYear());
        assertEquals(TEST_FIELD.getChronology(), CHRONOLOGY);
        assertEquals( month, CHRONOLOGY.getMonthOfYear(ImmutableDateTime.now().getMillis()));
    }

    @Test
    public void testAddZero() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.add(instant, 0);
        assertEquals(instant, result);
    }

    @Test
    public void testAddOne() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.add(instant, 1);
        assertEquals(TEST_FIELD.getChronology().getMillisPerYear(), TEST_FIELD.getAmount(instant, 1));
        assertEquals(result, TEST_FIELD.getChronology().getMillis(CHRONOLOGY.getYear(instant),
                TEST_FIELD.get(instant) + 1));
    }

    @Test
    public void testAddNegativeOne() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.add(instant, -1);
        assertEquals(TEST_FIELD.getChronology().getMillisPerYear(), TEST_FIELD.getAmount(instant, -1));
        assertEquals(result, TEST_FIELD.getChronology().getMillis(CHRONOLOGY.getYear(instant),
                TEST_FIELD.get(instant) - 1));
    }

    @Test
    public void testAddValueOutOfRange() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.add(instant, 12 * 12);
        int year = TEST_FIELD.getChronology().getYear(instant);
        int month = TEST_FIELD.get(instant);
        int newMonth = month + TEST_FIELD.getAmount(instant, 12 * 12);
        long expected = TEST_FIELD.getChronology().getMillis(year + 1, newMonth);
        assertEquals(expected, result);
    }

    @Test
    public void testAddMonths() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.add(instant, 12);
        int year = TEST_FIELD.getChronology().getYear(instant);
        int month = TEST_FIELD.get(instant);
        int newMonth = month + 12;
        long expected = TEST_FIELD.getChronology().getMillis(year + 1, newMonth);
        assertEquals(expected, result);
    }

    @Test
    public void testAddDays() {
        long instant = ImmutableDateTime.now().getMillis();
        int daysToAdd = 365 + 12 * 30;
        long result = TEST_FIELD.add(instant, daysToAdd);
        int year = TEST_FIELD.getChronology().getYear(instant);
        int month = TEST_FIELD.get(instant);
        int newYear = year + 1;
        int newMonth = 1;
        long expected = TEST_FIELD.getChronology().getMillis(newYear, newMonth);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapping() {
        long instant = ImmutableDateTime.now().getMillis();
        int monthsToAdd = 13;
        long result = TEST_FIELD.addWrapField(instant, monthsToAdd);
        int year = TEST_FIELD.getChronology().getYear(instant);
        int month = TEST_FIELD.get(instant);
        int newMonth = FieldUtils.getWrappedValue(month, monthsToAdd, 1, 12);
        long expected = TEST_FIELD.getChronology().getMillis(year, newMonth);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifference() {
        long minuendInstant = ImmutableDateTime.now().getMillis();
        long subtrahendInstant = ImmutableDateTime.now().plusDays(1).getMillis();
        long result = TEST_FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, result);
    }

    @Test
    public void testSet() {
        long instant = ImmutableDateTime.now().getMillis();
        int monthToSet = 5;
        long result = TEST_FIELD.set(instant, monthToSet);
        int expectedYear = TEST_FIELD.getChronology().getYear(instant);
        int expectedMonth = monthToSet;
        long expectedMillis = TEST_FIELD.getChronology().getMillis(expectedYear, expectedMonth);
        assertEquals(expectedMillis, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        long instant = ImmutableDateTime.now().getMillis();
        int monthToSet = 13;
        TEST_FIELD.set(instant, monthToSet);
    }

    @Test
    public void testGetRangeDurationField() {
        assertEquals(DurationField.DAYS, TEST_FIELD.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        long instant = ImmutableDateTime.now().getMillis();
        boolean result = TEST_FIELD.isLeap(instant);
        assertFalse(result);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = ImmutableDateTime.now().getMillis();
        int result = TEST_FIELD.getLeapAmount(instant);
        assertEquals(0, result);
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(DurationField.DAYS, TEST_FIELD.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, TEST_FIELD.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, TEST_FIELD.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.roundFloor(instant);
        long expected = TEST_FIELD.getChronology().getYearMonthMillis
                (CHRONOLOGY.getYear(instant), TEST_FIELD.get(instant));
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        long instant = ImmutableDateTime.now().getMillis();
        long result = TEST_FIELD.remainder(instant);
        long expected = Instant.now().getMillis() % (TEST_FIELD.getChronology().getMillisPerYear());
        assertEquals(expected, result);
    }

    @Test
    public void testAddReadablePartial() {
        ReadableDateTime dateTime = new LocalDateTime(2023, 3, 1, 12, 0, 0);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        int[] values = {3, 15};
        int fieldIndex = JulianFields.MONTH_OF_YEAR.getFieldIndex();
        long result = field.add(dateTime.toLocalDateTime(), fieldIndex, values, 1);
        assertEquals(new LocalDateTime(2023, 4, 15, 12, 0, 0).toDate().getTime(), result);
    }

    @Test
    public void testAddReadablePartialWrapping() {
        ReadableDateTime dateTime = new LocalDateTime(2023, 3, 1, 12, 0, 0);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        int[] values = {13, 5};
        int fieldIndex = JulianFields.MONTH_OF_YEAR.getFieldIndex();
        long result = field.add(dateTime.toLocalDateTime(), fieldIndex, values, 1);
        assertEquals(new LocalDateTime(2024, 5, 5, 12, 0, 0).toDate().getTime(), result);
    }
}