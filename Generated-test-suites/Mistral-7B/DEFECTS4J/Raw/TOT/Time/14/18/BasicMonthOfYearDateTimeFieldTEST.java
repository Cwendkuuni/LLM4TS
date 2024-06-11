import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.Instant;
import org.joda.time.JodaTimeUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMinMaxValues() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2);

        // Minimum value
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.JANUARY, field.get(0L));

        // Maximum value
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.get(Long.MAX_VALUE - 1));
    }

    @Test
    public void testSetGet() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2);

        // Set and get month
        long instant = JodaTimeUtils.millis(2010, 2, 28);
        long newInstant = field.set(instant, DateTimeConstants.APRIL);
        assertEquals(2010, iChronology.getYear(newInstant));
        assertEquals(DateTimeConstants.APRIL, field.get(newInstant));

        // Leap month
        long leapInstant = JodaTimeUtils.millis(2012, 2, 29);
        long newLeapInstant = field.set(leapInstant, DateTimeConstants.FEBRUARY);
        assertEquals(2012, iChronology.getYear(newLeapInstant));
        assertEquals(DateTimeConstants.FEBRUARY, field.get(newLeapInstant));
    }

    @Test
    public void testAdd() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2);

        // Add one month
        long instant = JodaTimeUtils.millis(2010, 2, 28);
        long newInstant = field.add(instant, 1);
        assertEquals(2010, iChronology.getYear(newInstant));
        assertEquals(DateTimeConstants.MARCH, field.get(newInstant));

        // Add twelve months
        newInstant = field.add(instant, 12);
        assertEquals(2011, iChronology.getYear(newInstant));
        assertEquals(DateTimeConstants.JANUARY, field.get(newInstant));

        // Add negative months
        newInstant = field.add(instant, -1);
        assertEquals(2010, iChronology.getYear(newInstant));
        assertEquals(DateTimeConstants.FEBRUARY, field.get(newInstant));

        // Add months to maximum value
        newInstant = field.add(Long.MAX_VALUE - 1, 1);
        assertEquals(Long.MAX_VALUE, newInstant);
        assertEquals(DateTimeConstants.DECEMBER, field.get(newInstant));
    }

    @Test
    public void testGetMonthOfYear() {
        BasicChronology gregorian = new BasicGregorianChronology();
        Instant instant = new Instant(0, 0, 0, 0, 1, 2010, 0);

        assertEquals(DateTimeConstants.JANUARY, iChronology.getMonthOfYear(instant));
        instant = new Instant(0, 0, 0, 0, 12, 2010, 0);
        assertEquals(DateTimeConstants.DECEMBER, iChronology.getMonthOfYear(instant));

        instant = new Instant(0, 0, 0, 0, 59, 2010, 0); // Last day of February
        if (iChronology.isLeapYear(iChronology.getYear(instant))) {
            assertEquals(DateTimeConstants.FEBRUARY, iChronology.getMonthOfYear(instant));
        } else {
            assertEquals(DateTimeConstants.FEBRUARY, iChronology.getMonthOfYear(instant) - 1);
        }
    }

    @Test
    public void testAddWrapField() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 12);

        long instant = JodaTimeUtils.millis(2010, 12, 31);
        long newInstant = field.addWrapField(instant, 1);
        assertEquals(2011, iChronology.getYear(newInstant));
        assertEquals(DateTimeConstants.JANUARY, field.get(newInstant));

        instant = JodaTimeUtils.millis(1900, 1, 1);
        newInstant = field.addWrapField(instant, -13);
        assertEquals(1887, iChronology.getYear(newInstant));
        assertEquals(DateTimeConstants.DECEMBER, field.get(newInstant));
    }

    @Test
    public void testGetDifference() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 12);

        long instant1 = JodaTimeUtils.millis(1990, 12, 31);
        long instant2 = JodaTimeUtils.millis(1991, 1, 31);

        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(1, difference);

        instant2 = JodaTimeUtils.millis(1990, 11, 30);
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(2, difference);

        instant1 = JodaTimeUtils.millis(1999, 2, 29);
        instant2 = JodaTimeUtils.millis(2000, 2, 29);
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(0, difference);
    }

    @Test
    public void testIsLeap() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2);

        // Not a leap year
        long instant = JodaTimeUtils.millis(1995, 2, 28);
        assertFalse(field.isLeap(instant));

        // Leap year
        instant = JodaTimeUtils.millis(2000, 2, 29);
        assertTrue(field.isLeap(instant));

        // Negative instant
        instant = JodaTimeUtils.millis(-1);
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2);

        // Not a leap year
        long instant = JodaTimeUtils.millis(1995, 2, 28);
        assertEquals(0, field.getLeapAmount(instant));

        // Leap year
        instant = JodaTimeUtils.millis(2000, 2, 29);
        assertEquals(1, field.getLeapAmount(instant));

        // Negative instant
        instant = JodaTimeUtils.millis(-1);
        assertEquals(0, field.getLeapAmount(instant));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2);

        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), leapDurationField.getType());
        assertEquals(1, leapDurationField.getValues()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 12);

        long instant = JodaTimeUtils.millis(2010, 12, 31);
        long newInstant = field.set(instant, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidMonths() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 12);

        long instant = JodaTimeUtils.millis(2010, 12, 31);
        long newInstant = field.add(instant, -13);
    }

    @Test
    public void testGetDifference() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 12);

        long instant1 = JodaTimeUtils.millis(1990, 12, 31);
        long instant2 = JodaTimeUtils.millis(1991, 1, 31);

        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(1, difference);

        instant2 = JodaTimeUtils.millis(1990, 11, 30);
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(2, difference);

        instant1 = JodaTimeUtils.millis(1999, 2, 29);
        instant2 = JodaTimeUtils.millis(2000, 2, 29);
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(0, difference);

        // Test with local date
        LocalDate date1 = new LocalDate(1990, 12, 31);
        LocalDate date2 = new LocalDate(1991, 1, 31);
        difference = field.getDifferenceAsLong(date1.toInstant().getMillis(), date2.toInstant().getMillis());
        assertEquals(1, difference);
    }
}