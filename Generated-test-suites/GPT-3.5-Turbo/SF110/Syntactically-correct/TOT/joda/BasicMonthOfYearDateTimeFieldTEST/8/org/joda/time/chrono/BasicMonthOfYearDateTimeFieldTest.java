import org.junit.*;
import static org.junit.Assert.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        int expectedOutput = 8;
        assertEquals(expectedOutput, field.get(input));
    }

    @Test
    public void testAddInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        int amount = 3;
        long expectedOutput = 1601522400000L; // Represents the date 2020-11-04
        assertEquals(expectedOutput, field.add(input, amount));
    }

    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        long amount = 2592000000L; // Represents 30 days
        long expectedOutput = 1624437600000L; // Represents the date 2021-06-04
        assertEquals(expectedOutput, field.add(input, amount));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        int amount = 8; // Wrap around to same month of next year
        long expectedOutput = 1627615200000L; // Represents the date 2021-08-04
        assertEquals(expectedOutput, field.addWrapField(input, amount));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long start = 1596520800000L; // Represents the date 2020-08-04
        long end = 1627615200000L; // Represents the date 2021-08-04
        long expectedOutput = 12;
        assertEquals(expectedOutput, field.getDifferenceAsLong(start, end));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        int value = 11;
        long expectedOutput = 1635852000000L; // Represents the date 2021-11-04
        assertEquals(expectedOutput, field.set(input, value));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long leapYearDate = 1609455600000L; // Represents the date 2021-01-01 (leap year)
        long nonLeapYearDate = 1607143200000L; // Represents the date 2020-12-05 (non-leap year)
        assertTrue(field.isLeap(leapYearDate));
        assertFalse(field.isLeap(nonLeapYearDate));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long leapYearDate = 1609455600000L; // Represents the date 2021-01-01 (leap year)
        long nonLeapYearDate = 1607143200000L; // Represents the date 2020-12-05 (non-leap year)
        int expectedLeapAmount = 1;
        int expectedNonLeapAmount = 0;
        assertEquals(expectedLeapAmount, field.getLeapAmount(leapYearDate));
        assertEquals(expectedNonLeapAmount, field.getLeapAmount(nonLeapYearDate));
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        int expectedMinimumValue = 1;
        assertEquals(expectedMinimumValue, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        int expectedMaximumValue = 12;
        assertEquals(expectedMaximumValue, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        long expectedOutput = 1593592800000L; // Represents the date 2020-07-01
        assertEquals(expectedOutput, field.roundFloor(input));
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
        long input = 1596520800000L; // Represents the date 2020-08-04
        long expectedOutput = 292800000L; // Represents the time 04:08
        assertEquals(expectedOutput, field.remainder(input));
    }

}