import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;

    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 2);
    }

    @Test
    public void testIsLenient() {
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long millis = 1577836800L;
        int expected = 1;
        int actual = field.get(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_multipleMonths() {
        long millis = 1577836800L;
        int monthsToAdd = 5;
        long expected = 1593475200L;
        long actual = field.add(millis, monthsToAdd);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_zeroMonths() {
        long millis = 1577836800L;
        int monthsToAdd = 0;
        long expected = 1577836800L;
        long actual = field.add(millis, monthsToAdd);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_negativeMonths() {
        long millis = 1577836800L;
        int monthsToAdd = -3;
        long expected = 1573036800L;
        long actual = field.add(millis, monthsToAdd);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_largePositiveMonths() {
        long millis = 1577836800L; 
        int monthsToAdd = Integer.MAX_VALUE;
        long expected = 3333334400L;
        long actual = field.add(millis, monthsToAdd);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd_largeNegativeMonths() {
        long millis = 1577836800L;
        int monthsToAdd = Integer.MIN_VALUE;
        long expected = -2222228800L;
        long actual = field.add(millis, monthsToAdd);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField_positive() {
        long millis = 1577836800L;
        int months = 5;
        long expected = 1611484800L;
        long actual = field.addWrapField(millis, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField_positive_max() {
        long millis = 1577836800L;
        int months = Integer.MAX_VALUE;
        long expected = -964025600L;
        long actual = field.addWrapField(millis, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField_negative() {
        long millis = 1577836800L;
        int months = -3;
        long expected = 1573036800L;
        long actual = field.addWrapField(millis, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField_negative_max() {
        long millis = 1577836800L;
        int months = Integer.MIN_VALUE;
        long expected = -2082804800L;
        long actual = field.addWrapField(millis, months);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long millis1 = 1577836800L;
        long millis2 = 1611484800L;
        long expected = 3;
        long actual = field.getDifferenceAsLong(millis2, millis1);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_largeMonths() {
        long millis = 1577836800L;
        long monthsToAdd = 10000000000L;
        field.add(millis, monthsToAdd);
    }

    @Test
    public void testSet() {
        long millis = 1577836800L;
        int month = 5;
        long expected = 1590902400L;
        long actual = field.set(millis, month);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_invalidMonth() {
        long millis = 1577836800L;
        int month = Integer.MAX_VALUE;
        field.set(millis, month);
    }

    @Test
    public void testIsLeap_leapMonth() {
        long millis = 1577836800L;
        boolean expected = true;
        boolean actual = field.isLeap(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap_nonLeapMonth() {
        long millis = 1577836800L;
        field = new BasicMonthOfYearDateTimeField(chronology, 3);
        boolean expected = false;
        boolean actual = field.isLeap(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount_leapMonth() {
        long millis = 1577836800L;
        int expected = 1;
        int actual = field.getLeapAmount(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount_nonLeapMonth() {
        long millis = 1577836800L;
        field = new BasicMonthOfYearDateTimeField(chronology, 3);
        int expected = 0;
        int actual = field.getLeapAmount(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        Assert.assertNotNull(durationField);
        Assert.assertEquals(chronology.years(), durationField);
    }
    
    @Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        Assert.assertNotNull(durationField);
        Assert.assertEquals(chronology.days(), durationField);
    }

    @Test
    public void testGetMinimumValue() {
        int expected = 1;
        int actual = field.getMinimumValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = chronology.getMaxMonth();
        int actual = field.getMaximumValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long millis = 1577836800L;
        long expected = 1577836800L;
        long actual = field.roundFloor(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        long millis = 1577836800L;
        long expected = 0L;
        long actual = field.remainder(millis);
        Assert.assertEquals(expected, actual);
    }
}