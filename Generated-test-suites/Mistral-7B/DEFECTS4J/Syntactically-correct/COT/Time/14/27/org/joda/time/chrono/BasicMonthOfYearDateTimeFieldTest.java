import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.JodaTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, 8, 15);
        int actual = field.get(instant);
        int expected = DateTimeConstants.AUGUST;
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        long expected = JodaTime.readMilliseconds(2022, DateTimeConstants.FEBRUARY, 28);
        long result = field.add(instant, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_long_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        long months = 2;
        long expected = JodaTime.readMilliseconds(2022, DateTimeConstants.MARCH, 31);
        long result = field.add(instant, months);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, 12, 31);
        int months = 3;
        long expected = JodaTime.readMilliseconds(2023, 2, 28);
        long result = field.addWrapField(instant, months);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDifferenceAsLong_long_long_exception() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long minuendInstant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        long subtrahendInstant = JodaTime.readMilliseconds(2022, DateTimeConstants.DECEMBER, 31);
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testSet_long_int() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        int month = DateTimeConstants.MARCH;
        long expected = JodaTime.readMilliseconds(2022, DateTimeConstants.MARCH, 31);
        long result = field.set(instant, month);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.FEBRUARY, 28);
        boolean actual = field.isLeap(instant);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.FEBRUARY, 29);
        int actual = field.getLeapAmount(instant);
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        int actual = field.getMinimumValue();
        int expected = DateTimeConstants.JANUARY;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        int actual = field.getMaximumValue();
        int expected = DateTimeConstants.DECEMBER;
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 15);
        long expected = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 15);
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder_long() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 15);
        long expected = 0;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerialization() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        // Serialize
        // Write to a ByteArrayOutputStream, and convert it to a byte array
        // for testing
        // ...
        // Deserialize
        BasicMonthOfYearDateTimeField deserializedField = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        // Check equality
        assertEquals(field, deserializedField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_long_int_exception() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.DECEMBER, 32);
        int month = DateTimeConstants.JANUARY;
        field.set(instant, month);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapField_long_int_exception() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.DECEMBER, 31);
        int months = 13;
        field.addWrapField(instant, months);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_long_int_exception2() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        int month = -1;
        field.set(instant, month);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_long_int_exception1() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        int months = -1;
        field.add(instant, months);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_long_int_exception2() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        int months = 13;
        field.add(instant, months);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_int_exception() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = JodaTime.readMilliseconds(2022, DateTimeConstants.JANUARY, 31);
        int month = -1;
        field.get(instant);
    }
}