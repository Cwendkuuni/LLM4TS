import org.joda.time.BasicChronology;
import org.joda.time.ChronologyException;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.JodaTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());
        int month = field.get(instant);

        assertEquals(DateTimeConstants.JANUARY, month);

        instant += DateTimeConstants.MILLIS_PER_DAY;
        month = field.get(instant);

        assertEquals(DateTimeConstants.FEBRUARY, month);
    }

    @Test
    public void testAdd_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());

        // Add one month
        long newInstant = field.add(instant, 1);
        LocalDate date = new LocalDate(newInstant);

        int expectedYear = DateTimeConstants.GET_CURRENT_YEAR();
        int expectedMonth = DateTimeConstants.FEBRUARY;
        int expectedDayOfMonth = DateTimeConstants.FIRST_DAY_OF_MONTH;

        assertEquals(expectedYear, date.getYear());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date.getDayOfMonth());

        // Add 11 months
        newInstant = field.add(instant, 11);
        date = new LocalDate(newInstant);

        expectedYear += 11;
        expectedMonth = DateTimeConstants.DECEMBER;
        expectedDayOfMonth = DateTimeConstants.LAST_DAY_OF_MONTH;

        assertEquals(expectedYear, date.getYear());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date.getDayOfMonth());
    }

    @Test(expected = ChronologyException.class)
    public void testAdd_long_int_bounds() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());

        int maxMonth = new BasicGregorianChronology().getMaxMonth();
        int minMonth = new BasicGregorianChronology().getMinMonth();

        // Add one more month than possible
        long newInstant = field.add(instant, maxMonth + 1);
    }

    @Test(expected = ChronologyException.class)
    public void testSet_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());

        int minMonth = new BasicGregorianChronology().getMinMonth();
        int maxMonth = new BasicGregorianChronology().getMaxMonth();

        // Set month to minimum value
        long newInstant = field.set(instant, minMonth);

        LocalDate date = new LocalDate(newInstant);

        int expectedYear = DateTimeConstants.GET_CURRENT_YEAR();
        int expectedMonth = minMonth;
        int expectedDayOfMonth = DateTimeConstants.FIRST_DAY_OF_MONTH;

        assertEquals(expectedYear, date.getYear());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date.getDayOfMonth());

        // Set month to maximum value
        newInstant = field.set(instant, maxMonth);

        date = new LocalDate(newInstant);

        expectedYear += 11;
        expectedMonth = maxMonth;
        expectedDayOfMonth = DateTimeConstants.LAST_DAY_OF_MONTH;

        assertEquals(expectedYear, date.getYear());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date.getDayOfMonth());

        // Set month to invalid value
        long invalidInstant = field.set(instant, -1);

        date = new LocalDate(invalidInstant);

        fail("Expected ChronologyException");
    }

    @Test
    public void testAddWrapField_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());

        int maxMonth = new BasicGregorianChronology().getMaxMonth();
        int minMonth = new BasicGregorianChronology().getMinMonth();

        // Add one more month than possible, wrapping around
        long newInstant = field.addWrapField(instant, maxMonth + 1);

        LocalDate date = new LocalDate(newInstant);

        int expectedYear = DateTimeConstants.getPreviousYear(DateTimeConstants.GET_CURRENT_YEAR());
        int expectedMonth = DateTimeConstants.DECEMBER;
        int expectedDayOfMonth = DateTimeConstants.LAST_DAY_OF_MONTH;

        assertEquals(expectedYear, date.getYear());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date.getDayOfMonth());
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant1 = JodaTime.readMilliseconds(System.currentTimeMillis() - 30 * DateTimeConstants.MILLIS_PER_DAY);
        long instant2 = JodaTime.readMilliseconds(System.currentTimeMillis());

        int months = field.getMonthsBetween(instant1, instant2);

        long difference = field.getDifferenceAsLong(instant1, instant2);

        assertEquals(months, difference);
    }

    @Test
    public void testSet_long_int_bounds() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());

        int minMonth = new BasicGregorianChronology().getMinMonth();
        int maxMonth = new BasicGregorianChronology().getMaxMonth();

        // Set month to minimum value
        long newInstant = field.set(instant, minMonth - 1);

        LocalDate date = new LocalDate(newInstant);

        int expectedYear = DateTimeConstants.GET_CURRENT_YEAR() - 1;
        int expectedMonth = minMonth;
        int expectedDayOfMonth = DateTimeConstants.FIRST_DAY_OF_MONTH;

        assertEquals(expectedYear, date.getYear());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date.getDayOfMonth());

        // Set month to maximum value
        newInstant = field.set(instant, maxMonth + 1);

        date = new LocalDate(newInstant);

        int expectedYear2 = DateTimeConstants.GET_CURRENT_YEAR();
        int expectedMonth2 = DateTimeConstants.JANUARY;
        int expectedDayOfMonth2 = DateTimeConstants.LAST_DAY_OF_MONTH;

        assertEquals(expectedYear2, date.getYear());
        assertEquals(expectedMonth2, date.getMonthOfYear());
        assertEquals(expectedDayOfMonth2, date.getDayOfMonth());

        // Set month to invalid value
        long invalidInstant = field.set(instant, 13);

        date = new LocalDate(invalidInstant);

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_ReadablePartial_int_int_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        LocalDate date1 = new LocalDate(0L);
        date1 = date1.withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(DateTimeConstants.FIRST_DAY_OF_MONTH);

        int numberToAdd = 2;

        int[] values = new int[1];
        values[0] = DateTimeConstants.JANUARY;

        int valueToAdd = 1;

        long[] result = field.add(date1, 0, values, valueToAdd);

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testAddWrapField_long_int_wrapsAround() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);

        long instant = JodaTime.readMilliseconds(System.currentTimeMillis());

        int maxMonth = new BasicGregorianChronology().getMaxMonth();
        int minMonth = new BasicGregorianChronology().getMinMonth();

        // Set month to maximum value
        long newInstant = field.set(instant, maxMonth);

        longWrapFieldTest(field, instant, newInstant, maxMonth + 1, minMonth);

        // Set month to minimum value
        newInstant = field.set(instant, minMonth);

        longWrapFieldTest(field, instant, newInstant, minMonth - 1, maxMonth);
    }

    private void longWrapFieldTest(BasicMonthOfYearDateTimeField field, long instant, long newInstant, int amountToAdd, int amountToWrap) {
        LocalDate date1 = new LocalDate(instant);
        LocalDate date2 = new LocalDate(newInstant);

        int expectedYear = DateTimeConstants.GET_CURRENT_YEAR();
        int expectedMonth = DateTimeConstants.JANUARY;
        int expectedDayOfMonth = DateTimeConstants.LAST_DAY_OF_MONTH;

        assertEquals(expectedYear, date1.getYear());
        assertEquals(expectedMonth, date1.getMonthOfYear());
        assertEquals(expectedDayOfMonth, date1.getDayOfMonth());

        int expectedNewYear = DateTimeConstants.GET_CURRENT_YEAR() + ((amountToAdd + amountToWrap) / (2 * new BasicGregorianChronology().getTotalMonths()));
        int expectedNewMonth = DateTimeConstants.JANUARY + ((amountToAdd + amountToWrap) % (2 * new BasicGregorianChronology().getTotalMonths()));
        int expectedNewDayOfMonth = DateTimeConstants.FIRST_DAY_OF_MONTH;

        assertEquals(expectedNewYear, date2.getYear());
        assertEquals(expectedNewMonth, date2.getMonthOfYear());
        assertEquals(expectedNewDayOfMonth, date2.getDayOfMonth());

        int expectedRemainder = ((amountToAdd + amountToWrap) % (2 * new BasicGregorianChronology().getTotalMonths()) + 2 * new BasicGregorianChronology().getTotalMonths()) % (2 * new BasicGregorianChronology().getTotalMonths());
        int expectedRemainingMonths = Math.abs((((expectedNewMonth - DateTimeConstants.JANUARY) % (2 * new BasicGregorianChronology().getTotalMonths())) + new BasicGregorianChronology().getTotalMonths()) % (2 * new BasicGregorianChronology().getTotalMonths()));

        long newNewInstant = field.addWrapField(newInstant, amountToAdd);

        LocalDate date3 = new LocalDate(newNewInstant);

        int expectedYear2 = expectedNewYear;
        int expectedMonth2 = expectedNewMonth;
        int expectedDayOfMonth2 = expectedDayOfMonth + ((expectedRemainingMonths - 1) / new BasicGregorianChronology().getMonthsPerYear()) * new BasicGregorianChronology().getDaysInMonthOfYear(expectedNewYear, expectedNewMonth);

        assertEquals(expectedYear2, date3.getYear());
        assertEquals(expectedMonth2, date3.getMonthOfYear());
        assertEquals(expectedDayOfMonth2, date3.getDayOfMonth());

        assertEquals(expectedRemainder, field.getMonthsBetween(newInstant, newNewInstant));
    }
}