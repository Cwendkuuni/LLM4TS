package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Fields;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testCreate() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), testField.getDataType());
        assertEquals(gregorian, testField.getChronology());
        assertEquals(DateTimeConstants.JANUARY, testField.getMax());
        assertFalse(testField.isLenient());
    }

    @Test
    public void testGetMinValue() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, testField.getMinimumValue());
    }

    @Test
    public void testGetMaxValue() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.DECEMBER, testField.getMaximumValue());
    }

    @Test
    public void testGetLeapAmount() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.FEBRUARY);
        assertEquals(0, testField.getLeapAmount(LocalDateTime.parse("2023-03-31T00:00:00.000Z").getMillis()));
        assertTrue(testField.getLeapAmount(LocalDateTime.parse("2024-02-29T00:00:00.000Z").getMillis()) > 0);
    }

    @Test
    public void testGetLeapDurationField() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.FEBRUARY);
        DurationField df = testField.getLeapDurationField();
        assertEquals(DurationFieldType.days(), df.getDataType());
        assertEquals(1, df.getLength());
    }

    @Test
    public void testGetRangeDurationField() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        DurationField df = testField.getRangeDurationField();
        assertEquals(DurationFieldType.years(), df.getDataType());
        assertEquals(1, df.getLength());
    }

    @Test
    public void testIsLeap() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.FEBRUARY);
        assertFalse(testField.isLeap(LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis()));
        assertTrue(testField.isLeap(LocalDateTime.parse("2024-02-29T00:00:00.000Z").getMillis()));

        // Test for invalid year
        assertFalse(testField.isLeap(Long.MIN_VALUE));
    }

    @Test
    public void testGet() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis();
        int expected = DateTimeConstants.FEBRUARY;
        int actual = testField.get(testTime);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2023-03-01T00:00:00.000Z").getMillis();
        long actual = testField.set(testTime, DateTimeConstants.MARCH);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2023-03-02T00:00:00.000Z").getMillis();
        long actual = testField.add(testTime, 1);
        assertEquals(expected, actual);

        expected = LocalDateTime.parse("2023-01-31T00:00:00.000Z").getMillis();
        actual = testField.add(testTime, -1);
        assertEquals(expected, actual);

        testTime = LocalDateTime.parse("2023-12-31T00:00:00.000Z").getMillis();
        expected = LocalDateTime.parse("2024-01-01T00:00:00.000Z").getMillis();
        actual = testField.add(testTime, 1);
        assertEquals(expected, actual);

        // Test adding to a non-existent month
        testTime = LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis();
        expected = LocalDateTime.parse("2023-01-01T00:00:00.000Z").getMillis();
        actual = testField.add(testTime, 13);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-13-31T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2023-01-01T00:00:00.000Z").getMillis();
        long actual = testField.addWrapField(testTime, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapFieldNegative() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-01-01T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2023-12-31T00:00:00.000Z").getMillis();
        long actual = testField.addWrapField(testTime, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapFieldMaxValue() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.DECEMBER);
        long testTime = LocalDateTime.parse("2023-12-31T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2024-01-01T00:00:00.000Z").getMillis();
        long actual = testField.addWrapField(testTime, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapFieldMinValue() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-01-01T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2022-12-31T00:00:00.000Z").getMillis();
        long actual = testField.addWrapField(testTime, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddReadablePartial() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        ReadablePartial partial = new LocalDate();
        int[] values = new int[1];
        int fieldIndex = Fields.monthOfYear();
        int valueToAdd = 1;
        int[] newValues = testField.add(partial, fieldIndex, values, valueToAdd);
        assertEquals(DateTimeConstants.FEBRUARY, newValues[0]);
    }

    @Test
    public void testRoundFloor() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis();
        long expected = LocalDateTime.parse("2023-02-01T00:00:00.000Z").getMillis();
        long actual = testField.roundFloor(testTime);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-03-01T00:00:00.000Z").getMillis();
        long expected = 0;
        long actual = testField.remainder(testTime);
        assertEquals(expected, actual);

        testTime = LocalDateTime.parse("2023-02-28T00:00:00.000Z").getMillis();
        expected = 29;
        actual = testField.remainder(testTime);
        assertEquals(expected, actual);

        testTime = LocalDateTime.parse("2023-03-01T23:59:59.999Z").getMillis();
        expected = 1;
        actual = testField.remainder(testTime);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        Chronology gregorian = DateTimeUtils.gregorian();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long testTime = LocalDateTime.parse("2023-01-31T00:00:00.000Z").getMillis();
        testField.set(testTime, 14);
    }
}