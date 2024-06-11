package org.joda.time.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology ISO_CALENDAR = DateTimeUtils.getISOInstanteousCalendar();
    private static final int LEAP_MONTH = DateTimeConstants.FEBRUARY;
    private static final int NON_LEAP_MONTH = DateTimeConstants.APRIL;

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457074400000L);
        int expected = DateTimeConstants.APRIL;
        int actual = field.get(instant.getMillis());
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457074400000L);
        int monthsToAdd = 3;
        Instant expected = new Instant(1457140800000L); // June 1, 2016
        Instant actual = new Instant(field.add(instant.getMillis(), monthsToAdd).getTime());
        assertEquals(expected.getMillis(), actual.getMillis());
    }

    @Test
    public void testAddLeapYear() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457308800000L); // February 29, 2017
        int monthsToAdd = 12;
        Instant expected = new Instant(1457585600000L); // March 1, 2018
        Instant actual = new Instant(field.add(instant.getMillis(), monthsToAdd).getTime());
        assertEquals(expected.getMillis(), actual.getMillis());
    }

    @Test
    public void testAddWrap() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457074400000L); // April 1, 2016
        int monthsToAdd = 12;
        Instant expected = new Instant(1456927200000L); // March 31, 2016
        Instant actual = new Instant(field.addWrapField(instant.getMillis(), monthsToAdd).getTime());
        assertEquals(expected.getMillis(), actual.getMillis());
    }

    @Test
    public void testAddMonths() {
        LocalDateTime localDateTime = new LocalDateTime(2016, 3, 31, 0, 0);
        ReadablePartial field = ISO_CALENDAR.monthOfYear();
        LocalDateTime expected = new LocalDateTime(2016, 6, 30, 0, 0);
        LocalDateTime actual = field.addMonths(localDateTime, 3);
        assertEquals(expected.toDate().getTime(), actual.toDate().getTime());
    }

    @Test
    public void testGetMinValue() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @Test
    public void testGetMaxValue() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(ISO_CALENDAR.years(), rangeDurationField);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457074400000L);
        assertTrue(field.isLeap(instant.getMillis()));
        Instant nonLeapInstant = new Instant(1457131200000L); // March 31, 2016
        assertFalse(field.isLeap(nonLeapInstant.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457308800000L); // February 29, 2017
        assertEquals(1, field.getLeapAmount(instant.getMillis()));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(ISO_CALENDAR.days(), leapDurationField);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457074400000L); // April 1, 2016
        long expected = ISO_CALENDAR.getYearMonthMillis(2016, 4);
        long actual = field.roundFloor(instant.getMillis());
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = ISO_CALENDAR.monthOfYear();
        Instant instant = new Instant(1457074400000L); // April 1, 2016
        long expected = 0;
        long actual = field.remainder(instant.getMillis());
        assertEquals(expected, actual);
    }
}