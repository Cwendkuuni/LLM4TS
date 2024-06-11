import org.joda.time.*;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology isoChronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeFieldType.january());

        // Test zero value
        long zero = 0L;
        assertEquals(1, monthOfYear.get(zero));

        // Test valid values
        long jan1 = DateTimeConstants.MINUTE_OF_DAY;
        long jan31 = DateTimeConstants.MILLIS_PER_DAY;
        long feb28 = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH;
        long feb29 = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH + 1;
        long mar31 = DateTimeConstants.MILLIS_PER_DAY * 2 + DateTimeConstants.DAYS_PER_MONTH - 1;
        long dec31 = DateTimeConstants.MILLIS_PER_DAY * 11 + DateTimeConstants.DAYS_PER_MONTH - 1;

        assertEquals(1, monthOfYear.get(jan1));
        assertEquals(1, monthOfYear.get(jan31));
        assertEquals(2, monthOfYear.get(feb28));
        assertEquals(2, monthOfYear.get(feb29));
        assertEquals(3, monthOfYear.get(mar31));
        assertEquals(12, monthOfYear.get(dec31));

        // Test invalid values
        long beforeMin = DateTimeConstants.MILLIS_PER_DAY * (-1) - 1;
        long maxValue = DateTimeConstants.MILLIS_PER_DAY * (DateTimeConstants.NUM_YEARS + 1) * DateTimeConstants.DAYS_PER_MONTH - 1;

        try {
            monthOfYear.get(beforeMin);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            monthOfYear.get(maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test addition
        long jan1PlusOne = jan1 + DateTimeConstants.MILLIS_PER_DAY;
        long jan1PlusOneMonth = monthOfYear.add(jan1, 1);
        assertEquals(2, monthOfYear.get(jan1PlusOneMonth));

        // Test addition with negative months
        long jan1MinusOneMonth = monthOfYear.add(jan1, -1);
        assertEquals(1, monthOfYear.get(jan1MinusOneMonth));

        // Test addition with large number of months
        long jan1PlusTwelveMonths = monthOfYear.add(jan1, 12);
        assertEquals(13, monthOfYear.get(jan1PlusTwelveMonths));

        // Test subtraction
        long jan1MinusOneMonthSubtracted = monthOfYear.add(jan1PlusOneMonth, -1);
        assertEquals(1, monthOfYear.get(jan1MinusOneMonthSubtracted));

        // Test subtraction with negative months
        long jan1MinusOneMonthSubtractedNegative = monthOfYear.add(jan1PlusOneMonth, -2);
        assertEquals(11, monthOfYear.get(jan1MinusOneMonthSubtractedNegative));

        // Test subtraction with large number of months
        long jan1MinusTwelveMonths = monthOfYear.add(jan1PlusTwelveMonths, -12);
        assertEquals(1, monthOfYear.get(jan1MinusTwelveMonths));

        // Test addition with large number of months and check for wrapped value
        long jan1PlusTwentyFourMonths = monthOfYear.add(jan1, 24);
        assertEquals(1, monthOfYear.get(jan1PlusTwentyFourMonths));

        // Test subtraction with large number of months and check for wrapped value
        long jan1MinusTwentyFourMonths = monthOfYear.add(jan1PlusTwentyFourMonths, -24);
        assertEquals(1, monthOfYear.get(jan1MinusTwentyFourMonths));

        // Test isLeap method
        long feb29InLeapYear = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH + 1;
        assertTrue(monthOfYear.isLeap(feb29InLeapYear));

        long feb28InNonLeapYear = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH;
        assertFalse(monthOfYear.isLeap(feb28InNonLeapYear));

        // Test getLeapAmount method
        assertEquals(1, monthOfYear.getLeapAmount(feb29InLeapYear));
        assertEquals(0, monthOfYear.getLeapAmount(feb28InNonLeapYear));

        // Test getLeapDurationField method
        DurationField leapDurationField = monthOfYear.getLeapDurationField();
        assertEquals(1, leapDurationField.getMillisWithoutAdjustment(1));

        // Test getDifferenceAsLong method
        long minuend = DateTimeConstants.MILLIS_PER_DAY;
        long subtrahend = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH;
        long difference = monthOfYear.getDifferenceAsLong(minuend, subtrahend);
        assertEquals(-1, difference);

        // Test set method
        long jan1Set = monthOfYear.set(jan1, 2);
        assertEquals(2, monthOfYear.get(jan1Set));

        // Test addWrapField method
        long jan1AddWrap = monthOfYear.addWrapField(jan1, 13);
        assertEquals(1, monthOfYear.get(jan1AddWrap));

        // Test get method with null argument
        try {
            monthOfYear.get(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // Test add method with null arguments
        try {
            monthOfYear.add(null, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // Test add method with Long.MIN_VALUE argument
        try {
            monthOfYear.add(Long.MIN_VALUE, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormat() {
        BasicChronology isoChronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeFieldType.january());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");

        long jan1 = DateTimeConstants.MILLIS_PER_DAY;
        DateTime date1 = new DateTime(jan1);
        String formatted1 = formatter.print(date1);
        assertEquals("Jan 2000", formatted1);

        long jan31 = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH - 1;
        DateTime date31 = new DateTime(jan31);
        String formatted31 = formatter.print(date31);
        assertEquals("Jan 2000", formatted31);

        long feb28 = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH;
        DateTime date28 = new DateTime(feb28);
        String formatted28 = formatter.print(date28);
        assertEquals("Feb 2000", formatted28);

        long feb29 = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_MONTH + 1;
        DateTime date29 = new DateTime(feb29);
        String formatted29 = formatter.print(date29);
        assertEquals("Feb 2000", formatted29);

        long mar31 = DateTimeConstants.MILLIS_PER_DAY * 2 + DateTimeConstants.DAYS_PER_MONTH - 1;
        DateTime date31Mar = new DateTime(mar31);
        String formatted31Mar = formatter.print(date31Mar);
        assertEquals("Mar 2000", formatted31Mar);

        long dec31 = DateTimeConstants.MILLIS_PER_DAY * 11 + DateTimeConstants.DAYS_PER_MONTH - 1;
        DateTime date31Dec = new DateTime(dec31);
        String formatted31Dec = formatter.print(date31Dec);
        assertEquals("Dec 2000", formatted31Dec);
    }
}