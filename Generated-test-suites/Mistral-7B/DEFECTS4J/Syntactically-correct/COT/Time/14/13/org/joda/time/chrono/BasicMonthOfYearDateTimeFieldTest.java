package org.joda.time.chrono.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

/**
 * Test class for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology CHRONOLOGY = new BasicChronology("UTC", DateTimeConstants.UTC_DAYS_PER_YEAR) {
        @Override
        public long getMillisOfDay(long instant) {
            return super.getMillisOfDay(instant) % (DateTimeConstants.DAYS_PER_YEAR);
        }
    };

    private static final BasicMonthOfYearDateTimeField MONTH_OF_YEAR = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    @org.junit.Test
    public void testIsLenient() {
        assertFalse(MONTH_OF_YEAR.isLenient());
    }

    @org.junit.Test
    public void testGet() {
        long instant = BaseDateTime.UTC.getMillis();
        int month = MONTH_OF_YEAR.get(instant);
        assertEquals(DateTimeConstants.JANUARY, month);
    }

    @org.junit.Test
    public void testAdd() {
        long instant = BaseDateTime.UTC.getMillis();

        long result = MONTH_OF_YEAR.add(instant, 1);
        assertEquals(DateTimeConstants.JANUARY + 1, MONTH_OF_YEAR.get(result));

        result = MONTH_OF_YEAR.add(instant, -1);
        assertEquals(DateTimeConstants.JANUARY - 1, MONTH_OF_YEAR.get(result));

        result = MONTH_OF_YEAR.add(instant, DateTimeConstants.FEBRUARY_LENGTH + 1);
        assertEquals(DateTimeConstants.MARCH, MONTH_OF_YEAR.get(result));

        result = MONTH_OF_YEAR.add(instant, DateTimeConstants.FEBRUARY_LENGTH - 1);
        assertEquals(DateTimeConstants.FEBRUARY, MONTH_OF_YEAR.get(result));

        result = MONTH_OF_YEAR.add(instant, Integer.MIN_VALUE);
        assertEquals(DateTimeConstants.DECEMBER, MONTH_OF_YEAR.get(result));

        result = MONTH_OF_YEAR.add(instant, Integer.MAX_VALUE);
        assertEquals(DateTimeConstants.JANUARY, MONTH_OF_YEAR.get(result));
    }

    @org.junit.Test
    public void testAddWrapField() {
        long instant = BaseDateTime.UTC.getMillis();

        long result = MONTH_OF_YEAR.addWrapField(instant, 12);
        assertEquals(DateTimeConstants.JANUARY + 12, MONTH_OF_YEAR.get(result));

        instant = LocalDateTime.parse("2022-12-31T23:59:59.999Z").toInstant().getEpochMilli();
        result = MONTH_OF_YEAR.addWrapField(instant, 1);
        assertEquals(DateTimeConstants.JANUARY, MONTH_OF_YEAR.get(result));
    }

    @org.junit.Test
    public void testGetDifferenceAsLong() {
        long instant1 = BaseDateTime.UTC.getMillis();
        long instant2 = BaseDateTime.UTC.getMillis() + (DateTimeConstants.MONTHS_PER_YEAR * 2);

        assertEquals(2, MONTH_OF_YEAR.getDifferenceAsLong(instant1, instant2));
    }

    @org.junit.Test
    public void testSet() {
        long instant = BaseDateTime.UTC.getMillis();

        long result = MONTH_OF_YEAR.set(instant, DateTimeConstants.DECEMBER);
        assertEquals(DateTimeConstants.DECEMBER, MONTH_OF_YEAR.get(result));

        instant = LocalDateTime.parse("2022-12-31T23:59:59.999Z").toInstant().getEpochMilli();
        result = MONTH_OF_YEAR.set(instant, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, MONTH_OF_YEAR.get(result));
    }

    @org.junit.Test
    public void testGetRangeDurationField() {
        DurationField durationField = MONTH_OF_YEAR.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, durationField.getUnit());
        assertEquals(1, durationField.getAmount());
    }

    @org.junit.Test
    public void testIsLeap() {
        long instant = BaseDateTime.UTC.getMillis();
        assertFalse(MONTH_OF_YEAR.isLeap(instant));

        instant = LocalDateTime.parse("2022-02-28T23:59:59.999Z").toInstant().getEpochMilli();
        assertTrue(MONTH_OF_YEAR.isLeap(instant));
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        long instant = BaseDateTime.UTC.getMillis();
        assertFalse(MONTH_OF_YEAR.isLeap(instant));

        instant = LocalDateTime.parse("2022-02-29T23:59:59.999Z").toInstant().getEpochMilli();
        assertEquals(1, MONTH_OF_YEAR.getLeapAmount(instant));
    }

    @org.junit.Test
    public void testGetLeapDurationField() {
        DurationField durationField = MONTH_OF_YEAR.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, durationField.getUnit());
        assertEquals(1, durationField.getAmount());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetMinimumValueNegative() {
        MONTH_OF_YEAR.getMinimumValue();
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, MONTH_OF_YEAR.getMinimumValue());
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, MONTH_OF_YEAR.getMaximumValue());
    }

    @org.junit.Test
    public void testRoundFloor() {
        long instant = BaseDateTime.UTC.getMillis();
        long roundFloor = MONTH_OF_YEAR.roundFloor(instant);
        assertEquals(instant, roundFloor);

        instant += DateTimeConstants.DAYS_PER_MONTH;
        roundFloor = MONTH_OF_YEAR.roundFloor(instant);
        assertEquals(instant - DateTimeConstants.MILLIS_PER_DAY, roundFloor);
    }

    @org.junit.Test
    public void testRemainder() {
        long instant = BaseDateTime.UTC.getMillis();
        long remainder = MONTH_OF_YEAR.remainder(instant);
        assertEquals(0, remainder);

        instant += DateTimeConstants.DAYS_PER_MONTH + 1;
        remainder = MONTH_OF_YEAR.remainder(instant);
        assertEquals(instant % (DateTimeConstants.DAYS_PER_MONTH + 1), remainder);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetInvalidValue() {
        long instant = BaseDateTime.UTC.getMillis();
        MONTH_OF_YEAR.set(instant, DateTimeConstants.FEBRUARY + 13);
    }

    @org.junit.Test
    public void testAddInvalidValues() {
        long instant = BaseDateTime.UTC.getMillis();
        long result = MONTH_OF_YEAR.add(instant, Integer.MIN_VALUE);
        assertEquals(instant, result);

        result = MONTH_OF_YEAR.add(instant, Integer.MAX_VALUE - 1);
        assertEquals(instant, result);

        result = MONTH_OF_YEAR.add(instant, Integer.MAX_VALUE);
        assertEquals(instant, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddWithInvalidFieldIndex() {
        ReadablePartial readablePartial = new LocalDate();
        MONTH_OF_YEAR.add(readablePartial, -1, new int[] {-1, DateTimeFieldType.MONTH_OF_YEAR.getCode()}, 1);
    }

    @org.junit.Test
    public void testAddWithValidFieldIndex() {
        ReadablePartial readablePartial = new LocalDate();
        int[] values = new int[]{DateTimeFieldType.MONTH_OF_YEAR.getCode(), 1};
        int[] result = MONTH_OF_YEAR.add(readablePartial, 1, values, 2);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR.getCode(), result[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR.getCode(), result[1]);

        LocalDate localDate = LocalDate.parse("2022-01-31");
        result = MONTH_OF_YEAR.add(localDate.toDateMidnightAtStartOfDay(), -1, values, 12);
        LocalDate expectedLocalDate = LocalDate.parse("2021-12-31");
        assertEquals(expectedLocalDate.toDateMidnightAtStartOfDay().getTime(), result[0].getTimeInMillis());
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR.getCode(), result[1]);
    }
}