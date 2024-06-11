package org.joda.time.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The chronology to use. */
    private static final BasicChronology CHRONOLOGY = new GregorianChronology();

    /** The field to test. */
    private static final BasicMonthOfYearDateTimeField FIELD =
        new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    /**
     * Test the basic get and set methods.
     */
    @Test
    public void testBasicMethods() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));
        instant = FIELD.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(instant));
        assertEquals(DateTimeConstants.FEBRUARY, FIELD.get(instant + DurationField.ONE.getMillis()));
    }

    /**
     * Test adding months with valid input.
     */
    @Test
    public void testAddMonthsValid() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        for (int i = 0; i < 12; i++) {
            instant = FIELD.add(instant, 1);
            assertEquals(i + 1, FIELD.get(instant));
        }
        for (int i = 1; i >= 0; i--) {
            instant = FIELD.add(instant, -1);
            assertEquals(12 - i, FIELD.get(instant));
        }
    }

    /**
     * Test adding months with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMonthsInvalid() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        FIELD.add(instant, Integer.MIN_VALUE);
    }

    /**
     * Test adding months with integer overflow.
     */
    @Test
    public void testAddMonthsOverflow() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int maxMonth = CHRONOLOGY.getMaxMonth();
        // Add one month more than the maximum
        long overflowInstant = FIELD.add(instant, maxMonth + 1);
        // Check that the month rolls over to the minimum
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(overflowInstant));
        // Check that the year increases by one
        assertEquals(CHRONOLOGY.getMaxYear() + 1, CHRONOLOGY.getYear(overflowInstant));
    }

    /**
     * Test adding months with long overflow.
     */
    @Test
    public void testAddMonthsLongOverflow() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int maxMonth = CHRONOLOGY.getMaxMonth();
        // Add one month more than the maximum
        long overflowInstant = FIELD.add(instant, Long.MAX_VALUE);
        // Check that the month rolls over to the minimum
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(overflowInstant));
        // Check that the year increases by one
        assertEquals(CHRONOLOGY.getMaxYear() + 1, CHRONOLOGY.getYear(overflowInstant));
    }

    /**
     * Test adding months to a date with a non-leap year.
     */
    @Test
    public void testAddMonthsNonLeapYear() {
        long instant = DateTimeConstants.FEBRUARY_28_2000;
        long expected = DateTimeConstants.MAY_31_2000;
        long result = FIELD.add(instant, 3);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a leap year.
     */
    @Test
    public void testAddMonthsLeapYear() {
        long instant = DateTimeConstants.FEBRUARY_29_2000;
        long expected = DateTimeConstants.MAY_31_2001;
        long result = FIELD.add(instant, 3);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a non-leap year and February 28th.
     */
    @Test
    public void testAddMonthsNonLeapYearFebruary28() {
        long instant = DateTimeConstants.FEBRUARY_28_2000;
        long expected = DateTimeConstants.APRIL_30_2000;
        long result = FIELD.add(instant, 2);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a leap year and February 29th.
     */
    @Test
    public void testAddMonthsLeapYearFebruary29() {
        long instant = DateTimeConstants.FEBRUARY_29_2000;
        long expected = DateTimeConstants.MAY_31_2001;
        long result = FIELD.add(instant, 3);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a non-leap year and February 28th and February 29th.
     */
    @Test
    public void testAddMonthsNonLeapYearFebruary28And29() {
        long instant = DateTimeConstants.FEBRUARY_28_2000;
        long expected = DateTimeConstants.APRIL_30_2001;
        long result = FIELD.add(instant, 2);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a leap year and February 28th and February 29th.
     */
    @Test
    public void testAddMonthsLeapYearFebruary28And29() {
        long instant = DateTimeConstants.FEBRUARY_29_2000;
        long expected = DateTimeConstants.MAY_31_2002;
        long result = FIELD.add(instant, 3);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a non-leap year, February 28th, and a negative month value.
     */
    @Test
    public void testAddMonthsNonLeapYearFebruary28Negative() {
        long instant = DateTimeConstants.FEBRUARY_28_2000;
        long expected = DateTimeConstants.DECEMBER_31_1999;
        long result = FIELD.add(instant, -2);
        assertEquals(expected, result);
    }

    /**
     * Test adding months to a date with a leap year, February 29th, and a negative month value.
     */
    @Test
    public void testAddMonthsLeapYearFebruary29Negative() {
        long instant = DateTimeConstants.FEBRUARY_29_2000;
        long expected = DateTimeConstants.NOVEMBER_30_1999;
        long result = FIELD.add(instant, -3);
        assertEquals(expected, result);
    }

    /**
     * Test the addWrapField method.
     */
    @Test
    public void testAddWrapField() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        for (int i = DateTimeConstants.JANUARY; i <= DateTimeConstants.DECEMBER; i++) {
            instant = FIELD.addWrapField(instant, i);
            assertEquals(i, FIELD.get(instant));
        }
        instant = FIELD.addWrapField(instant, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));
    }

    /**
     * Test the getDifference method.
     */
    @Test
    public void testGetDifference() {
        long instant1 = DateTimeConstants.MINUTE_OF_DAY;
        long instant2 = DateTimeConstants.HOURS_PER_DAY * 2 + DateTimeConstants.MINUTE_OF_DAY;
        long difference = FIELD.getDifferenceAsLong(instant1, instant2);
        assertEquals(-1, difference);
        difference = FIELD.getDifferenceAsLong(instant2, instant1);
        assertEquals(1, difference);
    }

    /**
     * Test the set method.
     */
    @Test
    public void testSet() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int month = DateTimeConstants.JANUARY;
        instant = FIELD.set(instant, month);
        assertEquals(month, FIELD.get(instant));
        instant = FIELD.set(instant, DateTimeConstants.DECEMBER);
        assertEquals(DateTimeConstants.DECEMBER, FIELD.get(instant));
    }

    /**
     * Test setting the month to a value less than the minimum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMinimum() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        FIELD.set(instant, DateTimeConstants.JANUARY - 1);
    }

    /**
     * Test setting the month to a value greater than the maximum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximum() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        FIELD.set(instant, DateTimeConstants.JANUARY + 1);
    }

    /**
     * Test rounding down.
     */
    @Test
    public void testRoundFloor() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));
        long roundFloor = FIELD.roundFloor(instant);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(roundFloor));
        instant -= 1000;
        roundFloor = FIELD.roundFloor(instant);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(roundFloor));
        instant -= 1000;
        assertEquals(DateTimeConstants.DECEMBER, FIELD.get(roundFloor));
    }

    /**
     * Test the remainder method.
     */
    @Test
    public void testRemainder() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        long remainder = FIELD.remainder(instant);
        assertEquals(0, remainder);
        instant -= 1000;
        remainder = FIELD.remainder(instant);
        assertEquals(DateTimeConstants.JANUARY, FIELD.get(instant));
        assertEquals(1, FIELD.getDifferenceAsLong(instant, DateTimeConstants.MINUTE_OF_DAY));
        assertEquals(-1, remainder);
    }
}