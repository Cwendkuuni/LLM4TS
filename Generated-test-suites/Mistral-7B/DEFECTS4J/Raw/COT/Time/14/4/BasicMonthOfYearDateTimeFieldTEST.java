package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.FieldVerifier;
import org.joda.time.field.Lists;
import org.joda.time.field.NullField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicFunctionality() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);

        // Test get(long)
        long instant = BaseDateTime.UTC.getMillis();
        int month = testField.get(instant);
        assertEquals("Test get(long)", DateTimeConstants.DECEMBER, month);

        // Test add(long, int)
        long newInstant = testField.add(instant, 1);
        month = testField.get(newInstant);
        assertEquals("Test add(long, int)", DateTimeConstants.JANUARY, month);

        // Test isLenient()
        assertFalse("Test isLenient()", testField.isLenient());

        // Test get(ReadableDateTime)
        ReadableDateTime testDate = new LocalDateTime(2012, 12, 12, 12, 0).toDateTime(DateTimeZone.UTC);
        month = testField.get(testDate.getMillis());
        assertEquals("Test get(ReadableDateTime)", DateTimeConstants.DECEMBER, month);

        // Test add(ReadableDateTime, int)
        LocalDateTime testDateTime = new LocalDateTime(2012, 12, 12, 12, 0);
        LocalDateTime newDateTime = testDateTime.plusMonths(1);
        newInstant = newDateTime.toDateTime(DateTimeZone.UTC).getMillis();
        month = testField.get(newInstant);
        assertEquals("Test add(ReadableDateTime, int)", DateTimeConstants.JANUARY, month);

        // Test add(ReadablePartial, int[])
        ReadablePartial testPartial = FieldUtils.localDate(newDateTime.toLocalDate());
        int[] values = new int[]{-1, 1};
        int[] newValues = testField.add(testPartial, this.getClass().getDeclaredField("FIELD_INDEX").getInteger(this), values, 1);
        assertEquals("Test add(ReadablePartial, int[])", 0, newValues[0]);
        assertEquals("Test add(ReadablePartial, int[])", 1, newValues[1]);

        // Test addWrapField(long, int)
        long wrapInstant = new DateTime(2012, 12, 32, 0, 0, 0).getMillis();
        newInstant = testField.addWrapField(instant, 1);
        month = testField.get(newInstant);
        assertEquals("Test addWrapField(long, int)", DateTimeConstants.JANUARY, month);

        // Test getDifferenceAsLong(long, long)
        long diffInstant = testField.getDifferenceAsLong(instant, newInstant);
        assertEquals("Test getDifferenceAsLong(long, long)", 1, diffInstant);

        // Test set(long, int)
        long setInstant = testField.set(instant, 1);
        month = testField.get(setInstant);
        assertEquals("Test set(long, int)", DateTimeConstants.JANUARY, month);

        // Test getRangeDurationField()
        DurationField durationField = testField.getRangeDurationField();
        assertEquals("Test getRangeDurationField()", DurationFieldType.YEARS, durationField.getType());

        // Test isLeap(long)
        int leapYear = 2012;
        boolean isLeap = testField.isLeap(new DateTime(leapYear, 0, 29, 0, 0, 0).getMillis());
        assertTrue("Test isLeap(long)", isLeap);

        // Test getLeapAmount(long)
        int leapAmount = testField.getLeapAmount(new DateTime(leapYear, 0, 29, 0, 0, 0).getMillis());
        assertEquals("Test getLeapAmount(long)", 1, leapAmount);

        // Test getLeapDurationField()
        DurationField leapDurationField = testField.getLeapDurationField();
        assertEquals("Test getLeapDurationField()", DurationFieldType.DAYS, leapDurationField.getType());

        // Test getMinimumValue()
        int min = testField.getMinimumValue();
        assertEquals("Test getMinimumValue()", DateTimeConstants.JANUARY, min);

        // Test getMaximumValue()
        int max = testField.getMaximumValue();
        assertEquals("Test getMaximumValue()", DateTimeConstants.DECEMBER, max);

        // Test roundFloor(long)
        long roundFloorInstant = testField.roundFloor(instant);
        assertSame("Test roundFloor(long)", instant, roundFloorInstant);

        // Test remainder(long)
        long remainderInstant = testField.remainder(instant);
        assertNotSame("Test remainder(long)", instant, remainderInstant);

        // Test readResolve()
        Object readResolve = testField.readResolve();
        assertTrue("Test readResolve()", (readResolve instanceof BasicMonthOfYearDateTimeField));
    }

    // Edge cases and exception handling scenarios

    // get(long)
    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeInstant() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        testField.get(-1);
    }

    @Test
    public void testGetMinYearInstant() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        LocalDate minDate = new LocalDate(chrono.getMinYear(), 1, 1);
        long minInstant = minDate.toDateTimeAtStartOfDay().getMillis();
        int month = testField.get(minInstant);
        assertEquals("Test getMinYearInstant", DateTimeConstants.JANUARY, month);
    }

    @Test
    public void testGetMaxYearInstant() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        LocalDate maxDate = new LocalDate(chrono.getMaxYear(), 12, 31);
        long maxInstant = maxDate.toDateTimeAtStartOfDay().getMillis();
        int month = testField.get(maxInstant);
        assertEquals("Test getMaxYearInstant", DateTimeConstants.DECEMBER, month);
    }

    // add(long, int)
    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeAmount() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        testField.add(0, -1);
    }

    @Test
    public void testAddMaxAmount() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        long instant = BaseDateTime.UTC.getMillis();
        long newInstant = testField.add(instant, testField.getMaximumValue());
        int month = testField.get(newInstant);
        assertEquals("Test addMaxAmount", DateTimeConstants.DECEMBER, month);
    }

    // add(long, long)
    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeAmountLong() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        testField.add(0, -1L);
    }

    @Test
    public void testAddMaxAmountLong() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        long instant = BaseDateTime.UTC.getMillis();
        long newInstant = testField.add(instant, Long.MAX_VALUE);
        int month = testField.get(newInstant);
        assertEquals("Test addMaxAmountLong", DateTimeConstants.DECEMBER, month);
    }

    // addWrapField(long, int)
    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapFieldNegativeAmount() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        testField.addWrapField(0, -1);
    }

    // getDifferenceAsLong(long, long)
    @Test(expected = IllegalArgumentException.class)
    public void testGetDifferenceNegativeMinuend() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        testField.getDifferenceAsLong(-1, 0);
    }

    // set(long, int)
    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMonth() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        testField.set(0, -1);
    }

    @Test
    public void testSetMaxValue() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        long instant = BaseDateTime.UTC.getMillis();
        long setInstant = testField.set(instant, testField.getMaximumValue());
        int month = testField.get(setInstant);
        assertEquals("Test setMaxValue", DateTimeConstants.DECEMBER, month);
    }

    // getMinimumValue() and getMaximumValue()
    @Test
    public void testMinimumValue() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        int min = testField.getMinimumValue();
        assertEquals("Test getMinimumValue()", DateTimeConstants.JANUARY, min);
    }

    @Test
    public void testMaximumValue() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(null, 0);
        int max = testField.getMaximumValue();
        assertEquals("Test getMaximumValue()", DateTimeConstants.DECEMBER, max);
    }

    // getLeapAmount(long)
    @Test
    public void testGetLeapAmountPositiveLeapYear() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        int leapAmount = testField.getLeapAmount(new DateTime(2000, 0, 29, 0, 0, 0).getMillis());
        assertEquals("Test getLeapAmountPositiveLeapYear", 1, leapAmount);
    }

    @Test
    public void testGetLeapAmountNonLeapYear() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        int leapAmount = testField.getLeapAmount(new DateTime(2000, 0, 28, 0, 0, 0).getMillis());
        assertEquals("Test getLeapAmountNonLeapYear", 0, leapAmount);
    }

    // getLeapDurationField()
    @Test
    public void testGetLeapDurationField() {
        BasicChronology chrono = new BasicChronology("TestChronology", DateTimeZone.UTC, DateTimeConstants.JANUARY, 12);
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, leapMonth);
        DurationField leapDurationField = testField.getLeapDurationField();
        assertEquals("Test getLeapDurationField()", DurationFieldType.DAYS, leapDurationField.getType());
    }

    // Merge all individual test cases into a complete JUnit 4 test file
}