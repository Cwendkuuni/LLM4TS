package org.joda.time.chrono.tests;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.LocalDateTimeUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The chronology for testing. */
    private static final Chronology CHRONOLOGY = new BasicChronology("MyChronology", DateTimeZone.UTC);

    /** The test field. */
    private static final BasicMonthOfYearDateTimeField FIELD =
        new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeFieldType.monthOfYear());

    /**
     * Test the basic get and set methods.
     */
    @Test
    public void testGetSet() {
        // Create initial DateTime.
        MutableDateTime dateTime = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);

        // Get the initial value.
        int initial = FIELD.get(dateTime.getMillis());
        Assert.assertEquals("Initial value incorrect", 3, initial);

        // Set a new value.
        int newValue = 5;
        dateTime.setField(DateTimeFieldType.monthOfYear(), newValue);

        // Get the new value.
        int newValueGet = FIELD.get(dateTime.getMillis());
        Assert.assertEquals("New value incorrect", newValue, newValueGet);

        // Set the same value.
        int sameValue = newValue;
        dateTime.setField(DateTimeFieldType.monthOfYear(), sameValue);

        // Get the same value.
        int sameValueGet = FIELD.get(dateTime.getMillis());
        Assert.assertEquals("Same value incorrect", sameValue, sameValueGet);
    }

    /**
     * Test the add methods.
     */
    @Test
    public void testAdd() {
        // Create initial DateTime.
        MutableDateTime dateTime = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);

        // Add a month.
        long millis = FIELD.add(dateTime.getMillis(), 1);
        ImmutableDateTime result = new ImmutableDateTime(millis);

        // Verify the result.
        LocalDateTime expected = LocalDateTime.of(2012, 4, 29, 12, 0, 0);
        LocalDateTime actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Add one month incorrect", expected, actual);

        // Add multiple months.
        millis = FIELD.add(dateTime.getMillis(), 12);
        result = new ImmutableDateTime(millis);

        // Verify the result.
        expected = LocalDateTime.of(2013, 3, 29, 12, 0, 0);
        actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Add twelve months incorrect", expected, actual);

        // Add a negative number of months.
        millis = FIELD.add(dateTime.getMillis(), -1);
        result = new ImmutableDateTime(millis);

        // Verify the result.
        expected = LocalDateTime.of(2012, 2, 29, 12, 0, 0);
        actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Add negative one month incorrect", expected, actual);

        // Add a large negative number of months.
        millis = FIELD.add(dateTime.getMillis(), -12);
        result = new ImmutableDateTime(millis);

        // Verify the result.
        expected = LocalDateTime.of(2011, 11, 29, 12, 0, 0);
        actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Add negative twelve months incorrect", expected, actual);
    }

    /**
     * Test the addWrapField methods.
     */
    @Test
    public void testAddWrapField() {
        // Create initial DateTime.
        MutableDateTime dateTime = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);

        // Add one month wrapping around.
        long millis = FIELD.addWrapField(dateTime.getMillis(), 1);
        ImmutableDateTime result = new ImmutableDateTime(millis);

        // Verify the result.
        LocalDateTime expected = LocalDateTime.of(2012, 2, 29, 12, 0, 0);
        LocalDateTime actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Add one month (wrap) incorrect", expected, actual);
    }

    /**
     * Test the getDifference methods.
     */
    @Test
    public void testGetDifference() {
        // Create initial DateTime.
        MutableDateTime dateTime1 = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);
        MutableDateTime dateTime2 = new MutableDateTime(2012, 4, 29, 12, 0, 0, 0);

        // Get the difference.
        long difference = FIELD.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());

        // Verify the difference.
        Assert.assertEquals("Difference incorrect", 1, difference);

        // Reverse the order and check again.
        difference = FIELD.getDifferenceAsLong(dateTime2.getMillis(), dateTime1.getMillis());

        // Verify the difference.
        Assert.assertEquals("Reversed difference incorrect", -1, difference);
    }

    /**
     * Test the set methods.
     */
    @Test
    public void testSet() {
        // Create initial DateTime.
        MutableDateTime dateTime = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);

        // Set a new value.
        int newValue = 5;
        long millis = FIELD.set(dateTime.getMillis(), newValue);

        // Verify the result.
        ImmutableDateTime result = new ImmutableDateTime(millis);
        LocalDateTime expected = LocalDateTime.of(2012, 5, 29, 12, 0, 0);
        LocalDateTime actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Set value incorrect", expected, actual);

        // Set the same value.
        int sameValue = newValue;
        millis = FIELD.set(dateTime.getMillis(), sameValue);

        // Verify the result.
        result = new ImmutableDateTime(millis);
        expected = LocalDateTime.of(2012, 5, 29, 12, 0, 0);
        actual = LocalDateTimeFieldVerifier.assertFieldsMatch(expected, result);
        Assert.assertEquals("Set same value incorrect", expected, actual);
    }

    /**
     * Test the getRangeDurationField methods.
     */
    @Test
    public void testGetRangeDurationField() {
        // Create the field.
        DurationField range = FIELD.getRangeDurationField();

        // Verify the result.
        Assert.assertEquals("Range duration incorrect", FIELD.getFieldType(), range.getFieldType());
        Assert.assertEquals("Range duration minimum value incorrect", 0, range.getMinimumValue().getValue());
        Assert.assertEquals("Range duration maximum value incorrect", Integer.MAX_VALUE, range.getMaximumValue().getValue());
    }

    /**
     * Test the isLeap methods.
     */
    @Test
    public void testIsLeap() {
        // Create initial DateTime.
        MutableDateTime dateTime = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);

        // Check for a non-leap year.
        Assert.assertFalse("Non-leap year incorrect", FIELD.isLeap(dateTime.getMillis()));

        // Set the year to a leap year.
        int leapYear = 2012;
        dateTime.setYear(leapYear);

        // Check for a leap year.
        Assert.assertTrue("Leap year incorrect", FIELD.isLeap(dateTime.getMillis()));

        // Set the month to February.
        int month = DateTimeConstants.FEBRUARY;
        dateTime.setMonthOfYear(month);

        // Check for a leap year and February.
        Assert.assertTrue("Leap year and February incorrect", FIELD.isLeap(dateTime.getMillis()));

        // Set the month to another month.
        dateTime.setMonthOfYear(DateTimeConstants.JANUARY);

        // Check for a leap year and another month.
        Assert.assertFalse("Leap year and another month incorrect", FIELD.isLeap(dateTime.getMillis()));
    }

    /**
     * Test the getLeapAmount methods.
     */
    @Test
    public void testGetLeapAmount() {
        // Create initial DateTime.
        MutableDateTime dateTime = new MutableDateTime(2012, 3, 29, 12, 0, 0, 0);

        // Check for a non-leap year.
        Assert.assertEquals("Non-leap year incorrect", 0, FIELD.getLeapAmount(dateTime.getMillis()));

        // Set the year to a leap year.
        int leapYear = 2012;
        dateTime.setYear(leapYear);

        // Check for a leap year.
        Assert.assertEquals("Leap year incorrect", 1, FIELD.getLeapAmount(dateTime.getMillis()));

        // Set the month to February.
        int month = DateTimeConstants.FEBRUARY;
        dateTime.setMonthOfYear(month);

        // Check for a leap year and February.
        Assert.assertEquals("Leap year and February incorrect", 1, FIELD.getLeapAmount(dateTime.getMillis()));

        // Set the month to another month.
        dateTime.setMonthOfYear(DateTimeConstants.JANUARY);

        // Check for a leap year and another month.
        Assert.assertEquals("Leap year and another month incorrect", 0, FIELD.getLeapAmount(dateTime.getMillis()));
    }

    /**
     * Test the getLeapDurationField methods.
     */
    @Test
    public void testGetLeapDurationField() {
        // Create the field.
        DurationField leap = FIELD.getLeapDurationField();

        // Verify the result.
        Assert.assertEquals("Leap duration incorrect", FIELD.getFieldType(), leap.getFieldType());
        Assert.assertEquals("Leap duration minimum value incorrect", 0, leap.getMinimumValue().getValue());
        Assert.assertEquals("Leap duration maximum value incorrect", 1, leap.getMaximumValue().getValue());
    }
}