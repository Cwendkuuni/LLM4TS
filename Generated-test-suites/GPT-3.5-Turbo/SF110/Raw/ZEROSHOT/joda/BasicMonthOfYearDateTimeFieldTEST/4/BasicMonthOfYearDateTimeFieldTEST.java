import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime = 1609459200000L; // January 1, 2021
        int expected = 1;
        int result = field.get(dateTime);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime = 1609459200000L; // January 1, 2021
        int amount = 2;
        long expected = 1612137600000L; // March 1, 2021
        long result = field.add(dateTime, amount);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime = 1609459200000L; // January 1, 2021
        int amount = 12;
        long expected = 1609459200000L; // January 1, 2021
        long result = field.addWrapField(dateTime, amount);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime1 = 1609459200000L; // January 1, 2021
        long dateTime2 = 1616400000000L; // March 23, 2021
        long expected = 2;
        long result = field.getDifferenceAsLong(dateTime1, dateTime2);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime = 1609459200000L; // January 1, 2021
        int value = 6; // June
        long expected = 1622476800000L; // June 1, 2021
        long result = field.set(dateTime, value);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long dateTime1 = 1609459200000L; // January 1, 2021
        long dateTime2 = 1622476800000L; // June 1, 2021
        assertFalse(field.isLeap(dateTime1));
        assertTrue(field.isLeap(dateTime2));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long dateTime1 = 1609459200000L; // January 1, 2021
        long dateTime2 = 1622476800000L; // June 1, 2021
        int expected1 = 0;
        int expected2 = 1;
        int result1 = field.getLeapAmount(dateTime1);
        int result2 = field.getLeapAmount(dateTime2);
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        int expected = 1;
        int result = field.getMinimumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        int expected = 12;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime = 1609459200000L; // January 1, 2021
        long expected = 1609459200000L; // January 1, 2021
        long result = field.roundFloor(dateTime);
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        long dateTime = 1609459200000L; // January 1, 2021
        long expected = 0L;
        long result = field.remainder(dateTime);
        assertEquals(expected, result);
    }
}