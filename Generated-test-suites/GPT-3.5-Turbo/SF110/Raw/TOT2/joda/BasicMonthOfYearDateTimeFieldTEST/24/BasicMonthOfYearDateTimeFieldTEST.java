import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long dateTime = 1609459200000L; // January 1, 2021
        assertEquals(1, field.get(dateTime));
    }

    @Test
    public void testAddInt() {
        long dateTime = 1609459200000L; // January 1, 2021
        long result = field.add(dateTime, 2);
        assertEquals(1612137600000L, result); // March 1, 2021
    }

    @Test
    public void testAddLong() {
        long dateTime = 1609459200000L; // January 1, 2021
        long result = field.add(dateTime, 61L);
        assertEquals(1627833600000L, result); // March 1, 2023
    }

    @Test
    public void testAddWrapField() {
        long dateTime = 1609459200000L; // January 1, 2021
        long result = field.addWrapField(dateTime, 12);
        assertEquals(1609459200000L, result); // January 1, 2021
    }

    @Test
    public void testGetDifferenceAsLong() {
        long dateTime1 = 1609459200000L; // January 1, 2021
        long dateTime2 = 1640995200000L; // January 1, 2022
        long result = field.getDifferenceAsLong(dateTime1, dateTime2);
        assertEquals(12, result); // 12 months difference
    }

    @Test
    public void testSet() {
        long dateTime = 1609459200000L; // January 1, 2021
        long result = field.set(dateTime, 6);
        assertEquals(1612137600000L, result); // June 1, 2021
    }

    @Test
    public void testIsLeap() {
        long leapDateTime = 1625097600000L; // June 1, 2021
        long nonLeapDateTime = 1640995200000L; // January 1, 2022

        assertTrue(field.isLeap(leapDateTime));
        assertFalse(field.isLeap(nonLeapDateTime));
    }

    @Test
    public void testGetLeapAmount() {
        long leapDateTime = 1625097600000L; // June 1, 2021
        long nonLeapDateTime = 1640995200000L; // January 1, 2022

        assertEquals(1, field.getLeapAmount(leapDateTime));
        assertEquals(0, field.getLeapAmount(nonLeapDateTime));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long dateTime = 1609459200000L; // January 1, 2021
        long result = field.roundFloor(dateTime);
        assertEquals(1609459200000L, result); // January 1, 2021
    }

    @Test
    public void testRemainder() {
        long dateTime = 1609459200000L; // January 1, 2021
        long result = field.remainder(dateTime);
        assertEquals(0, result);
    }
}