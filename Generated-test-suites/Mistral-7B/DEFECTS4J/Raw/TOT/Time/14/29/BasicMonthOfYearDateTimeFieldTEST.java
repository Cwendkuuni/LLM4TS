import org.joda.time.BasicDateTime;
import org.joda.time.BasicMonthOfYearDateTimeField;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.MutableDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /**
     * Test for basic functionality of the field.
     */
    @Test
    public void testBasic() {
        Chronology gregorianChronology = DateTimeUtils.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY);

        // Get values
        long instant = new BasicDateTime(2007, 10, 31, 23, 59, 0).getMillis();
        int month = field.get(instant);
        assertEquals(10, month);

        // Add values
        long newInstant = field.add(instant, 2);
        month = field.get(newInstant);
        assertEquals(12, month);

        newInstant = field.add(newInstant, -1);
        month = field.get(newInstant);
        assertEquals(11, month);

        // Add negative values
        newInstant = field.add(instant, -2);
        month = field.get(newInstant);
        assertEquals(8, month);

        // Set values
        long setInstant = field.set(instant, 5);
        month = field.get(setInstant);
        assertEquals(5, month);

        // Get minimum and maximum values
        int min = field.getMinimumValue();
        int max = field.getMaximumValue();
        assertEquals(DateTimeConstants.JANUARY, min);
        assertEquals(DateTimeConstants.DECEMBER, max);

        // Test isWrapped and isUnwrapped methods
        long wrappedInstant = field.set(instant, DateTimeConstants.JANUARY + 12).getMillis();
        assertTrue(field.isWrapped(instant));
        assertFalse(field.isWrapped(wrappedInstant));
        assertEquals(DateTimeConstants.JANUARY, field.get(wrappedInstant).getMonthOfYear());
        assertEquals(1, field.get(wrappedInstant).getDayOfMonth());

        // Test getDifferenceAsLong method
        long minuendInstant = new BasicDateTime(2007, 11, 30, 23, 59, 0).getMillis();
        long subtrahendInstant = new BasicDateTime(2007, 10, 31, 23, 59, 0).getMillis();
        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);

        // Test isLeap and getLeapAmount methods
        instant = new BasicDateTime(2000, 2, 29, 23, 59, 0).getMillis();
        assertTrue(field.isLeap(instant));
        assertEquals(1, field.getLeapAmount(instant));

        // Test getLeapDurationField method
        DurationField leapDuration = field.getLeapDurationField();
        assertEquals(DurationField.FIELD_DAYS, leapDuration.getUnit());
        assertEquals(1, leapDuration.getNumUnits());
    }

    /**
     * Test for edge cases.
     */
    @Test
    public void testEdgeCases() {
        Chronology gregorianChronology = DateTimeUtils.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY);

        // Test minimum and maximum values
        MutableDateTime minDate = new MutableDateTime();
        minDate.setYear(DateTimeConstants.MIN_YEAR);
        minDate.setMonthOfYear(field.getMinimumValue());
        minDate.setDayOfMonth(1);
        long minInstant = minDate.getMillis();
        long maxInstant = field.getMaximumValue();
        long maxDateInstant = new MutableDateTime().setYear(DateTimeConstants.MAX_YEAR).getMillis();

        assertTrue(field.isLeap(minInstant));
        assertEquals(1, field.getLeapAmount(minInstant));
        assertTrue(field.isLeap(maxInstant));
        assertEquals(1, field.getLeapAmount(maxInstant));

        // Test value out of bounds
        MutableDateTime outOfBoundsDate = new MutableDateTime();
        int maxValuePlusOne = field.getMaximumValue() + 1;
        outOfBoundsDate.setYear(DateTimeConstants.MAX_YEAR);
        outOfBoundsDate.setMonthOfYear(maxValuePlusOne);
        long outOfBoundsInstant = outOfBoundsDate.getMillis();

        assertNull(field.get(outOfBoundsInstant));
        assertNull(field.getLeapAmount(outOfBoundsInstant));
        assertFalse(field.isLeap(outOfBoundsInstant));

        // Test minimum value with negative years
        MutableDateTime negativeYearDate = new MutableDateTime();
        negativeYearDate.setYear(DateTimeConstants.MIN_YEAR - 1);
        negativeYearDate.setMonthOfYear(field.getMinimumValue());
        negativeYearDate.setDayOfMonth(1);
        long negativeYearInstant = negativeYearDate.getMillis();

        assertNull(field.get(negativeYearInstant));
        assertNull(field.getLeapAmount(negativeYearInstant));
        assertFalse(field.isLeap(negativeYearInstant));

        // Test maximum value with negative years
        MutableDateTime negativeMaxYearDate = new MutableDateTime();
        negativeMaxYearDate.setYear(DateTimeConstants.MAX_YEAR - 1);
        negativeMaxYearDate.setMonthOfYear(field.getMaximumValue());
        negativeMaxYearDate.setDayOfMonth(1);
        long negativeMaxYearInstant = negativeMaxYearDate.getMillis();

        assertNull(field.get(negativeMaxYearInstant));
        assertNull(field.getLeapAmount(negativeMaxYearInstant));
        assertFalse(field.isLeap(negativeMaxYearInstant));

        // Test minimum value with zero year
        MutableDateTime zeroYearDate = new MutableDateTime();
        zeroYearDate.setYear(0);
        zeroYearDate.setMonthOfYear(field.getMinimumValue());
        zeroYearDate.setDayOfMonth(1);
        long zeroYearInstant = zeroYearDate.getMillis();

        assertEquals(field.getMinimumValue(), field.get(zeroYearInstant));
        assertNull(field.getLeapAmount(zeroYearInstant));
        assertFalse(field.isLeap(zeroYearInstant));

        // Test maximum value with zero year
        MutableDateTime zeroMaxYearDate = new MutableDateTime();
        zeroMaxYearDate.setYear(0);
        zeroMaxYearDate.setMonthOfYear(field.getMaximumValue());
        zeroMaxYearDate.setDayOfMonth(1);
        long zeroMaxYearInstant = zeroMaxYearDate.getMillis();

        assertEquals(field.getMaximumValue(), field.get(zeroMaxYearInstant));
        assertNull(field.getLeapAmount(zeroMaxYearInstant));
        assertFalse(field.isLeap(zeroMaxYearInstant));
    }

    /**
     * Test for error cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testError() {
        Chronology gregorianChronology = DateTimeUtils.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY);

        long invalidInstant = new BasicDateTime(2007, 13, 31, 23, 59, 0).getMillis();
        field.get(invalidInstant);
    }
}