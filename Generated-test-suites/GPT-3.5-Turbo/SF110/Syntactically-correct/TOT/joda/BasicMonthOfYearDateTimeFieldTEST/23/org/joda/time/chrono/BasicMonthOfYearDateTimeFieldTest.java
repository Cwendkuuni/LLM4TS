import static org.junit.Assert.*;
import org.joda.time.*;
import org.junit.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    BasicChronology iChronology = new BasicChronology();
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        int expected = 4;
        assertEquals(expected, field.get(n));
    }
    
    @Test
    public void testAddWithZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        long expected = n;
        assertEquals(expected, field.add(n, 0));
    }
    
    @Test
    public void testAddWithPositiveValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        int n2 = 3;
        long expected = 1593568800000L;  // July 2, 2020 00:00:00
        assertEquals(expected, field.add(n, n2));
    }
    
    @Test
    public void testAddWithNegativeValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        int n2 = -4;
        long expected = 1580878400000L;  // December 26, 2019 00:00:00
        assertEquals(expected, field.add(n, n2));
    }
    
    @Test
    public void testAddWithNegativeValueWhenCrossingYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        int n2 = -5;
        long expected = 1575216000000L;  // November 1, 2019 00:00:00
        assertEquals(expected, field.add(n, n2));
    }
    
    @Test
    public void testAddWithLongValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        long n2 = 3L;
        long expected = 1593568800000L;  // July 2, 2020 00:00:00
        assertEquals(expected, field.add(n, n2));
    }
    
    @Test
    public void testAddWithLongValueWhenCrossingYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        long n2 = -5L;
        long expected = 1575216000000L;  // November 1, 2019 00:00:00
        assertEquals(expected, field.add(n, n2));
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        int n2 = 5;
        long expected = 1590962400000L;  // September 1, 2020 00:00:00
        assertEquals(expected, field.addWrapField(n, n2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithLargeAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        long n2 = 999999999999L;
        field.add(n, n2);
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        int n2 = 9;
        long expected = 1602127200000L;  // October 8, 2020 00:00:00
        assertEquals(expected, field.set(n, n2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        field.set(n, 13);
    }
    
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n1 = 1586287200000L;  // April 8, 2020 00:00:00
        long n2 = 1577836800000L;  // January 1, 2020 00:00:00
        assertTrue(field.isLeap(n1));
        assertFalse(field.isLeap(n2));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n1 = 1586287200000L;  // April 8, 2020 00:00:00
        long n2 = 1577836800000L;  // January 1, 2020 00:00:00
        assertEquals(1, field.getLeapAmount(n1));
        assertEquals(0, field.getLeapAmount(n2));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        int expected = 1;
        assertEquals(expected, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        int expected = 12;
        assertEquals(expected, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        long expected = 1585699200000L;  // April 1, 2020 00:00:00
        assertEquals(expected, field.roundFloor(n));
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n = 1586287200000L;  // April 8, 2020 00:00:00
        long expected = 685200000L;  // April 8, 2020 11:20:00
        assertEquals(expected, field.remainder(n));
    }
    
    @Test
    public void testAddOnReadablePartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        MutableDateTime dateTime = new MutableDateTime(2020, 4, 8, 0, 0, 0, 0);
        dateTime = field.add(dateTime, 1);
        DateTime expected = new DateTime(2020, 5, 8, 0, 0, 0, 0);
        assertEquals(expected, dateTime);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        long n1 = 1586287200000L;  // April 8, 2020 00:00:00
        long n2 = 1577836800000L;  // January 1, 2020 00:00:00
        long expected = 3;
        assertEquals(expected, field.getDifferenceAsLong(n1, n2));
    }
    
    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        DurationField expected = iChronology.years();
        assertEquals(expected, field.getRangeDurationField());
    }
    
    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        DurationField expected = iChronology.days();
        assertEquals(expected, field.getLeapDurationField());
    }
}