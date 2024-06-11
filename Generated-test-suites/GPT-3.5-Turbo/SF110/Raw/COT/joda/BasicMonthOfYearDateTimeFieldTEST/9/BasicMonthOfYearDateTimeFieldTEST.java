import org.joda.time.field.*;
import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        assertEquals(chronology.getMonthOfYear(n), field.get(n));
    }
    
    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int n2 = 2;
        long result = field.add(n, n2);
        assertEquals(n + (n2 * chronology.getAverageMillisPerMonth()), result);
    }
    
    @Test
    public void testAddInt_zero() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int n2 = 0;
        long result = field.add(n, n2);
        assertEquals(n, result);
    }
    
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        long n2 = 2;
        long result = field.add(n, n2);
        assertEquals(n + (n2 * chronology.getAverageMillisPerMonth()), result);
    }
    
    @Test
    public void testAddLong_int() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        long n2 = 2;
        int n3 = 3;
        long result = field.add(n, n2, n3);
        assertEquals(n + (n2 * chronology.getAverageMillisPerMonth()) + (n3 * chronology.getAverageMillisPerHour()), result);
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int n2 = 2;
        long result = field.addWrapField(n, n2);
        assertEquals(n + (n2 * chronology.getAverageMillisPerMonth()), result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        long set = n - (10L * chronology.getAverageMillisPerMonth());
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(10L, result);
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int n2 = 5;
        long result = field.set(n, n2);
        assertEquals(chronology.getYear(n), chronology.getYear(result));
        assertEquals(n2, chronology.getMonthOfYear(result));
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int year = chronology.getYear(n);
        int monthOfYear = chronology.getMonthOfYear(n, year);
        boolean expected = chronology.isLeapYear(year) && monthOfYear == 1;
        assertEquals(expected, field.isLeap(n));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int year = chronology.getYear(n);
        int monthOfYear = chronology.getMonthOfYear(n, year);
        int expected = (chronology.isLeapYear(year) && monthOfYear == 1) ? 1 : 0;
        assertEquals(expected, field.getLeapAmount(n));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        int year = chronology.getYear(n);
        long expected = chronology.getYearMonthMillis(year, chronology.getMonthOfYear(n, year));
        assertEquals(expected, field.roundFloor(n));
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = System.currentTimeMillis();
        long rounded = field.roundFloor(n);
        long expected = n - rounded;
        assertEquals(expected, field.remainder(n));
    }
}