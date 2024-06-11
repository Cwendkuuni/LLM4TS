import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Months;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

class BasicMonthOfYearDateTimeFieldTest {

    private BasicChronology chronology;
    private BasicMonthOfYearDateTimeField monthField;

    @Before
    public void setup() {
        chronology = new BasicChronology();
        monthField = new BasicMonthOfYearDateTimeField(chronology, 1);
    }

    // Test cases for isLenient()
    @Test
    public void testIsLenient() {
        assertFalse(monthField.isLenient());
    }

    // Test cases for get()
    @Test
    public void testGet_PositiveValue() {
        DateTime dateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        assertEquals(4, monthField.get(dateTime.getMillis()));
    }

    @Test
    public void testGet_NegativeValue() {
        DateTime dateTime = new DateTime(2021, 1, 15, 10, 0, 0);
        assertEquals(12, monthField.get(dateTime.getMillis()));
    }

    // Test cases for add(long n, int n2)
    @Test
    public void testAdd_PositiveValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.add(startDateTime.getMillis(), 2);
        DateTime expectedDateTime = startDateTime.plusMonths(2);
        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAdd_NegativeValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.add(startDateTime.getMillis(), -2);
        DateTime expectedDateTime = startDateTime.minusMonths(2);
        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAdd_ZeroValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.add(startDateTime.getMillis(), 0);
        assertEquals(startDateTime.getMillis(), result);
    }

    // Test cases for add(long n, long n2)
    @Test
    public void testAddWrapField_PositiveValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.addWrapField(startDateTime.getMillis(), 2);
        DateTime expectedDateTime = startDateTime.plusMonths(2);
        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAddWrapField_NegativeValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.addWrapField(startDateTime.getMillis(), -2);
        DateTime expectedDateTime = startDateTime.minusMonths(2);
        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testAddWrapField_ZeroValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.addWrapField(startDateTime.getMillis(), 0);
        assertEquals(startDateTime.getMillis(), result);
    }

    // Test cases for add(ReadablePartial readablePartial, int n, int[] array, int n2)
    @Test
    public void testAdd_ValidReadablePartialAndN() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        DateTime endDateTime = new DateTime(2022, 6, 20, 12, 0, 0);
        int[] values = {4};
        ReadablePartial partial = new DateTime(startDateTime.getMillis()).toMutableDateTime().toReadablePartial();
        int[] result = monthField.add(partial, 0, values, Months.monthsBetween(startDateTime, endDateTime).getMonths());
        int[] expectedValues = {6};
        assertArrayEquals(expectedValues, result);
    }

    @Test
    public void testAdd_InvalidReadablePartialAndN() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        int[] values = {4};
        ReadablePartial partial = new DateTime(startDateTime.getMillis()).toMutableDateTime().toReadablePartial();
        int[] result = monthField.add(partial, 1, values, 2);
        int[] expectedValues = {4};
        assertArrayEquals(expectedValues, result);
    }

    // Test cases for getDifferenceAsLong(long n, long set)
    @Test
    public void testGetDifferenceAsLong_PositiveDifference() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        DateTime endDateTime = new DateTime(2022, 6, 20, 12, 0, 0);
        long result = monthField.getDifferenceAsLong(endDateTime.getMillis(), startDateTime.getMillis());
        assertEquals(14, result);
    }

    @Test
    public void testGetDifferenceAsLong_NegativeDifference() {
        DateTime startDateTime = new DateTime(2022, 6, 20, 12, 0, 0);
        DateTime endDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.getDifferenceAsLong(endDateTime.getMillis(), startDateTime.getMillis());
        assertEquals(-14, result);
    }

    @Test
    public void testGetDifferenceAsLong_ZeroDifference() {
        DateTime dateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.getDifferenceAsLong(dateTime.getMillis(), dateTime.getMillis());
        assertEquals(0, result);
    }

    // Test cases for set(long n, int n2)
    @Test
    public void testSet_ValidValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.set(startDateTime.getMillis(), 6);
        DateTime expectedDateTime = startDateTime.withMonthOfYear(6);
        assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testSet_InvalidValue() {
        DateTime startDateTime = new DateTime(2021, 4, 15, 10, 0, 0);
        long result = monthField.set(startDateTime.getMillis(), -1);
        assertEquals(startDateTime.getMillis(), result);
    }

    // Test cases for isLeap(long n)
    @Test
    public void testIsLeap_LeapYearAndMatchingLeapMonth() {
        DateTime leapYearDateTime = new DateTime(2020, 2, 15, 10, 0, 0);
        assertTrue(monthField.isLeap(leapYearDateTime.getMillis()));
    }

    @Test
    public void testIsLeap_LeapYearAndNonMatchingLeapMonth() {
        DateTime leapYearDateTime = new DateTime(2020, 3, 15, 10, 0, 0);
        assertFalse(monthField.isLeap(leapYearDateTime.getMillis()));
    }

    @Test
    public void testIsLeap_NonLeapYearAndMatchingLeapMonth() {
        DateTime nonLeapYearDateTime = new DateTime(2021, 2, 15, 10, 0, 0);
        assertFalse(monthField.isLeap(nonLeapYearDateTime.getMillis()));
    }

    @Test
    public void testIsLeap_NonLeapYearAndNonMatchingLeapMonth() {
        DateTime nonLeapYearDateTime = new DateTime(2021, 3, 15, 10, 0, 0);
        assertFalse(monthField.isLeap(nonLeapYearDateTime.getMillis()));
    }

    // Test cases for getLeapAmount(long n)
    @Test
    public void testGetLeapAmount_LeapYearAndMatchingLeapMonth() {
        DateTime leapYearDateTime = new DateTime(2020, 2, 15, 10, 0, 0);
        assertEquals(1, monthField.getLeapAmount(leapYearDateTime.getMillis()));
    }

    @Test
    public void testGetLeapAmount_LeapYearAndNonMatchingLeapMonth() {
        DateTime leapYearDateTime = new DateTime(2020, 3, 15, 10, 0, 0);
        assertEquals(0, monthField.getLeapAmount(leapYearDateTime.getMillis()));
    }

    @Test
    public void testGetLeapAmount_NonLeapYearAndMatchingLeapMonth() {
        DateTime nonLeapYearDateTime = new DateTime(2021, 2, 15, 10, 0, 0);
        assertEquals(0, monthField.getLeapAmount(nonLeapYearDateTime.getMillis()));
    }

    @Test
    public void testGetLeapAmount_NonLeapYearAndNonMatchingLeapMonth() {
        DateTime nonLeapYearDateTime = new DateTime(2021, 3, 15, 10, 0, 0);
        assertEquals(0, monthField.getLeapAmount(nonLeapYearDateTime.getMillis()));
    }

    // Test cases for getMinimumValue()
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, monthField.getMinimumValue());
    }

    // Test cases for getMaximumValue()
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, monthField.getMaximumValue());
    }

}