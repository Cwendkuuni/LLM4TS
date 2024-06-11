import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private static BasicChronology iChronology;
    private int expectedMax;
    private int expectedLeapMonth;

    @Before
    public void setUp() {
        iChronology = new BasicChronology();
        expectedMax = iChronology.getMaxMonth();
        expectedLeapMonth = 5;
        field = new BasicMonthOfYearDateTimeField(iChronology, expectedLeapMonth);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long n = 1609455540000L; // January 1, 2021
        int expected = 1;
        int result = field.get(n);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_int_zero() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 0;
        long expected = n;
        long result = field.add(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_int_positive() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 3;
        long expected = 1617775540000L; // April 7, 2021
        long result = field.add(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_int_negative() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = -3;
        long expected = 1599775540000L; // September 17, 2020
        long result = field.add(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_long() {
        long n = 1609455540000L; // January 1, 2021
        long n2 = 3L;
        long expected = 1617775540000L; // April 7, 2021
        long result = field.add(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 3;
        long expected = 1601861140000L; // October 5, 2020
        long result = field.addWrapField(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long n = 1609455540000L; // January 1, 2021
        long set = 1599775540000L; // September 17, 2020
        long expected = 3L;
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 5;
        long expected = 1614623940000L; // February 30, 2021 (adjusted to February 28)
        long result = field.set(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        long leapYear = 1609455540000L; // January 1, 2021 (not a leap year)
        assertFalse(field.isLeap(leapYear));

        long nonLeapYear = 1641001140000L; // January 1, 2022 (a leap year)
        assertTrue(field.isLeap(nonLeapYear));
    }

    @Test
    public void testGetLeapAmount() {
        long leapYear = 1609455540000L; // January 1, 2021 (not a leap year)
        int expected = 0;
        int result = field.getLeapAmount(leapYear);
        assertEquals(expected, result);

        long nonLeapYear = 1641001140000L; // January 1, 2022 (a leap year)
        expected = 1;
        result = field.getLeapAmount(nonLeapYear);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMinimumValue() {
        int expected = 1;
        int result = field.getMinimumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = expectedMax;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testRoundFloor() {
        long n = 1609455540000L; // January 1, 2021
        long expected = 1609455540000L;
        long result = field.roundFloor(n);
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        long n = 1609455540000L; // January 1, 2021
        long expected = 0;
        long result = field.remainder(n);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWithReadablePartial() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 3;
        int[] array = {1};
        int n3 = 0;
        int[] expected = {4};
        int[] result = field.add(new BasicChronology(), iChronology.monthOfYear(), Arrays.copyOf(array, array.length), n2);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapField_outOfRange() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 10;
        field.addWrapField(n, n2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_outOfRange() {
        long n = 1609455540000L; // January 1, 2021
        int n2 = 13;
        field.set(n, n2);
    }

    @Parameterized.Parameters(name = "Leap Month: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {11}, {12}
        });
    }

    public BasicMonthOfYearDateTimeFieldTest(int expectedLeapMonth) {
        this.expectedLeapMonth = expectedLeapMonth;
    }

    @Test
    public void testConstructor_withLeapMonth() {
        BasicMonthOfYearDateTimeField newField = new BasicMonthOfYearDateTimeField(iChronology, expectedLeapMonth);
        assertEquals(expectedMax, newField.getMaximumValue());
        assertEquals(expectedLeapMonth, newField.iLeapMonth);
    }

    @Test
    public void testConstructor_withoutLeapMonth() {
        BasicMonthOfYearDateTimeField newField = new BasicMonthOfYearDateTimeField(iChronology, 0);
        assertEquals(expectedMax, newField.getMaximumValue());
        assertEquals(0, newField.iLeapMonth);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeLeapMonth() {
        new BasicMonthOfYearDateTimeField(iChronology, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidLeapMonth() {
        new BasicMonthOfYearDateTimeField(iChronology, 13);
    }

    @Test
    public void testGetRangeDurationField() {
        assertTrue(field.getRangeDurationField() instanceof DurationField);
        assertTrue(field.getRangeDurationField() instanceof BasicChronology);
    }

    @Test
    public void testGetLeapDurationField() {
        assertTrue(field.getLeapDurationField() instanceof DurationField);
        assertTrue(field.getLeapDurationField() instanceof BasicChronology);
    }

}