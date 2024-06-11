import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.apache.commons.lang3.ArrayUtils;

import static org.joda.time.DateTimeConstants.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final LocalDate SAMPLE_DATE = new LocalDate(2022, 6, 15);
    private static final int MAX_MONTH = Months.MAX_MONTH_OF_YEAR;
    private static final int MIN_MONTH = Months.MIN_MONTH_OF_YEAR;

    private BasicMonthOfYearDateTimeField monthField;

    @Before
    public void setUp() {
        monthField = new BasicMonthOfYearDateTimeField(new BasicChronology(), JUNE);
    }

    @Test
    public void testGet_basicCase() {
        assertEquals(JUNE, monthField.get(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void testGet_minMonth() {
        assertEquals(MIN_MONTH, monthField.get(new LocalDate(MIN_YEAR, 1, 1).toDateTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void testGet_maxMonth() {
        assertEquals(MAX_MONTH, monthField.get(new LocalDate(MAX_YEAR, 12, 31).toDateTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void testIsLenient() {
        assertFalse(monthField.isLenient());
    }

    @Test
    public void testAdd_basicCase() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 2);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(2);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAdd_sameMonth() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 0);
        assertEquals(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAdd_wrapAround() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 7);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(7);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAdd_beforeMinYear() {
        try {
            monthField.add(new LocalDate(MIN_YEAR, 1, 1).toDateTimeAtStartOfDay().getMillis(), -12);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testAdd_afterMaxYear() {
        try {
            monthField.add(new LocalDate(MAX_YEAR, 12, 31).toDateTimeAtStartOfDay().getMillis(), 12);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testAdd_long_basicCase() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 2L);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(2);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAdd_long_wrapAround() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 7L);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(7);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAdd_long_exactResult() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 12L);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(12);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAdd_long_fractionalValue() {
        long result = monthField.add(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 1.5);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(1);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAddWrapField_basicCase() {
        long result = monthField.addWrapField(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 2);
        LocalDate expectedDate = SAMPLE_DATE.plusMonths(2);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testAddWrapField_maxMonth() {
        long result = monthField.addWrapField(new LocalDate(MAX_YEAR, 12, 31).toDateTimeAtStartOfDay().getMillis(), 1);
        LocalDate expectedDate = new LocalDate(MAX_YEAR, 1, 1);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testGetDifferenceAsLong_basicCase() {
        long startDate = new LocalDate(2022, 1, 1).toDateTimeAtStartOfDay().getMillis();
        long endDate = new LocalDate(2022, 10, 1).toDateTimeAtStartOfDay().getMillis();
        long result = monthField.getDifferenceAsLong(startDate, endDate);
        assertEquals(9, result);
    }

    @Test
    public void testGetDifferenceAsLong_negativeResult() {
        long startDate = new LocalDate(2022, 10, 1).toDateTimeAtStartOfDay().getMillis();
        long endDate = new LocalDate(2022, 1, 1).toDateTimeAtStartOfDay().getMillis();
        long result = monthField.getDifferenceAsLong(startDate, endDate);
        assertEquals(-9, result);
    }

    @Test
    public void testSet_basicCase() {
        long result = monthField.set(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), 4);
        LocalDate expectedDate = SAMPLE_DATE.withMonthOfYear(4);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testSet_invalidMonth() {
        try {
            monthField.set(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis(), -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testSet_dayOfMonthExceedsMax() {
        long result = monthField.set(new LocalDate(2022, 1, 31).toDateTimeAtStartOfDay().getMillis(), 2);
        LocalDate expectedDate = new LocalDate(2022, 2, 28);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testGetRangeDurationField() {
        assertEquals(years(), monthField.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        long leapYearMillis = new LocalDate(2020, 6, 15).toDateTimeAtStartOfDay().getMillis();
        long nonLeapYearMillis = new LocalDate(2021, 6, 15).toDateTimeAtStartOfDay().getMillis();

        assertTrue(monthField.isLeap(leapYearMillis));
        assertFalse(monthField.isLeap(nonLeapYearMillis));
    }

    @Test
    public void testGetLeapAmount() {
        long leapYearMillis = new LocalDate(2020, 6, 15).toDateTimeAtStartOfDay().getMillis();
        long nonLeapYearMillis = new LocalDate(2021, 6, 15).toDateTimeAtStartOfDay().getMillis();

        assertEquals(1, monthField.getLeapAmount(leapYearMillis));
        assertEquals(0, monthField.getLeapAmount(nonLeapYearMillis));
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(days(), monthField.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(MIN_MONTH, monthField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(MAX_MONTH, monthField.getMaximumValue());
    }

    @Test
    public void testRoundFloor_basicCase() {
        long result = monthField.roundFloor(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis());
        LocalDate expectedDate = SAMPLE_DATE.withDayOfMonth(1);
        assertEquals(expectedDate.toDateTimeAtStartOfDay().getMillis(), result);
    }

    @Test
    public void testRemainder_basicCase() {
        long result = monthField.remainder(SAMPLE_DATE.toDateTimeAtStartOfDay().getMillis());
        assertEquals(0, result);
    }

    @Test
    public void testReadResolve() {
        Object result = monthField.readResolve();
        assertEquals(new BasicChronology().monthOfYear(), result);
    }

}