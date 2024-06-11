
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.LocalTime;
import org.joda.time.LocalDate;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        int expected = 5;
        assertEquals(expected, field.get(input));
    }
    
    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        int amount = 2;
        long expected = 1625097600000L; // July 2021
        assertEquals(expected, field.add(input, amount));
    }
    
    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        long amount = 2;
        long expected = 1625097600000L; // July 2021
        assertEquals(expected, field.add(input, amount));
    }
    
    @Test
    public void testAddReadablePartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        ReadablePartial input = new LocalDate(2021, 5, 15);
        int amount = 3;
        int[] values = { 2021, 5, 15 };
        int[] expected = { 2021, 8, 15 };
        assertArrayEquals(expected, field.add(input, amount, values, 2));
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        int amount = 10;
        long expected = 1627785600000L; // March 2022
        assertEquals(expected, field.addWrapField(input, amount));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long start = 1622505600000L; // May 2021
        long end = 1625097600000L; // July 2021
        long expected = 2;
        assertEquals(expected, field.getDifferenceAsLong(start, end));
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        int value = 10;
        long expected = 1640995200000L; // October 2022
        assertEquals(expected, field.set(input, value));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        int expected = 0;
        assertEquals(expected, field.getLeapAmount(input));
    }
    
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        long expected = 1619827200000L; // May 2021
        assertEquals(expected, field.roundFloor(input));
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long input = 1622505600000L; // May 2021
        long expected = 2678400000L; // 31 days in milliseconds
        assertEquals(expected, field.remainder(input));
    }
    
}