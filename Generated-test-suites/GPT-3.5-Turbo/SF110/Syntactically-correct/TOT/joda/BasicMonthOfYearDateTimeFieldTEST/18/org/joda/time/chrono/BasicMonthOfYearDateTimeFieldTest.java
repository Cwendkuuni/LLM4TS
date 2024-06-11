import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;
    
    @Before
    public void setUp() {
        BasicChronology basicChronology = new BasicChronology();
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, 6);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(basicMonthOfYearDateTimeField.isLenient());
    }
    
    @Test
    public void testGet() {
        long n = 1564316400000L; // 29 Jul 2019
        int expected = 7;
        int result = basicMonthOfYearDateTimeField.get(n);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithZero() {
        long n = 1564316400000L; // 29 Jul 2019
        int n2 = 0;
        long expected = 1564316400000L;
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithPositiveValue() {
        long n = 1564316400000L; // 29 Jul 2019
        int n2 = 7;
        long expected = 1568329200000L; // 29 Aug 2019
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithNegativeValue() {
        long n = 1564316400000L; // 29 Jul 2019
        int n2 = -2;
        long expected = 1559343600000L; // 31 May 2019
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapField() {
        long n = 1564316400000L; // 29 Jul 2019
        int n2 = 5;
        long expected = 1564316400000L; // 29 Jul 2019
        long result = basicMonthOfYearDateTimeField.addWrapField(n, n2);
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapFieldWithLargeMagnitude() {
        long n = 1564316400000L; // 29 Jul 2019
        int n2 = 15;
        basicMonthOfYearDateTimeField.addWrapField(n, n2);
        // Assertion will be handled by expected attribute
    }
    
    @Test
    public void testSet() {
        long n = 1564316400000L; // 29 Jul 2019
        int n2 = 12;
        long expected = 1575106800000L; // 30 Dec 2019
        long result = basicMonthOfYearDateTimeField.set(n, n2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsLeap() {
        long n = 1564316400000L; // 29 Jul 2019
        boolean result = basicMonthOfYearDateTimeField.isLeap(n);
        assertFalse(result);
    }
    
    @Test
    public void testIsLeapWithLeapYearAndValidLeapMonth() {
        long n = 1564316400000L; // 29 Jul 2019
        BasicChronology basicChronology = new BasicChronology() {
            public boolean isLeapYear(int year) {
                return true;
            }
        };
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, 7);
        boolean result = basicMonthOfYearDateTimeField.isLeap(n);
        assertTrue(result);
    }
    
    @Test
    public void testIsLeapWithLeapYearAndInvalidLeapMonth() {
        long n = 1564316400000L; // 29 Jul 2019
        BasicChronology basicChronology = new BasicChronology() {
            public boolean isLeapYear(int year) {
                return true;
            }
        };
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, 6);
        boolean result = basicMonthOfYearDateTimeField.isLeap(n);
        assertFalse(result);
    }
    
    @Test
    public void testGetLeapAmount() {
        long n = 1564316400000L; // 29 Jul 2019
        int result = basicMonthOfYearDateTimeField.getLeapAmount(n);
        assertEquals(0, result);
    }
    
    @Test
    public void testGetLeapAmountWithLeapYearAndValidLeapMonth() {
        long n = 1564316400000L; // 29 Jul 2019
        BasicChronology basicChronology = new BasicChronology() {
            public boolean isLeapYear(int year) {
                return true;
            }
        };
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, 7);
        int result = basicMonthOfYearDateTimeField.getLeapAmount(n);
        assertEquals(1, result);
    }
    
    @Test
    public void testGetRangeDurationField() {
        DurationField result = basicMonthOfYearDateTimeField.getRangeDurationField();
        assertNotNull(result);
    }
    
    @Test
    public void testGetDifferenceAsLongWithSameDates() {
        long n = 1564316400000L; // 29 Jul 2019
        long set = 1564316400000L; // 29 Jul 2019
        long result = basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set);
        assertEquals(0, result);
    }
    
    @Test
    public void testGetDifferenceAsLongWithGreaterDate() {
        long n = 1564316400000L; // 29 Jul 2019
        long set = 1567000200000L; // 29 Aug 2019
        long result = basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set);
        assertEquals(1, result);
    }
    
    @Test
    public void testGetDifferenceAsLongWithSmallerDate() {
        long n = 1564316400000L; // 29 Jul 2019
        long set = 1562143200000L; // 03 Jul 2019
        long result = basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set);
        assertEquals(-1, result);
    }
    
    @Test
    public void testRoundFloor() {
        long n = 1564316400000L; // 29 Jul 2019
        long expected = 1561935600000L; // 01 Jul 2019
        long result = basicMonthOfYearDateTimeField.roundFloor(n);
        assertEquals(expected, result);
    }
    
    @Test
    public void testRemainder() {
        long n = 1564316400000L; // 29 Jul 2019
        long expected = 23808000000L; // 22 Jul 2019
        long result = basicMonthOfYearDateTimeField.remainder(n);
        assertEquals(expected, result);
    }
    
}