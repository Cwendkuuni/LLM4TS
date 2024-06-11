import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.joda.time.LocalDate;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField dateTimeField;

    @Before
    public void setUp() {
        dateTimeField = new BasicMonthOfYearDateTimeField(new BasicChronology(), 7);
    }

    @Test
    public void testIsLenient() {
        Assert.assertFalse(dateTimeField.isLenient());
    }

    @Test
    public void testGet() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        Assert.assertEquals(1, dateTimeField.get(n));
    }

    @Test
    public void testAddWithZeroAmountShouldReturnSameValue() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        Assert.assertEquals(n, dateTimeField.add(n, 0));
    }

    @Test
    public void testAddWithPositiveAmountShouldReturnCorrectValue() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        long expected = new LocalDate(2019, 2, 28).toDate().getTime();
        Assert.assertEquals(expected, dateTimeField.add(n, 1));
    }

    @Test
    public void testAddWithNegativeAmountShouldReturnCorrectValue() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        long expected = new LocalDate(2018, 12, 31).toDate().getTime();
        Assert.assertEquals(expected, dateTimeField.add(n, -1));
    }

    @Test
    public void testAddWithLargeAmountShouldThrowException() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        try {
            dateTimeField.add(n, Integer.MAX_VALUE);
            Assert.fail("Expected Exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Magnitude of add amount is too large: 2147483647", e.getMessage());
        }
    }

    @Test
    public void testAddWrapField() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        long expected = new LocalDate(2019, 12, 31).toDate().getTime();
        Assert.assertEquals(expected, dateTimeField.addWrapField(n, 11));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long n1 = new LocalDate(2019, 1, 31).toDate().getTime();
        long n2 = new LocalDate(2018, 1, 31).toDate().getTime();
        Assert.assertEquals(0, dateTimeField.getDifferenceAsLong(n1, n1));
        Assert.assertEquals(12, dateTimeField.getDifferenceAsLong(n1, n2));
    }

    @Test
    public void testSet() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        long expected = new LocalDate(2019, 2, 28).toDate().getTime();
        Assert.assertEquals(expected, dateTimeField.set(n, 2));
    }

    @Test
    public void testIsLeap() {
        long n1 = new LocalDate(2019, 1, 31).toDate().getTime();
        long n2 = new LocalDate(2019, 7, 31).toDate().getTime();
        Assert.assertFalse(dateTimeField.isLeap(n1));
        Assert.assertTrue(dateTimeField.isLeap(n2));
    }

    @Test
    public void testGetLeapAmount() {
        long n1 = new LocalDate(2019, 1, 31).toDate().getTime();
        long n2 = new LocalDate(2019, 7, 31).toDate().getTime();
        Assert.assertEquals(0, dateTimeField.getLeapAmount(n1));
        Assert.assertEquals(1, dateTimeField.getLeapAmount(n2));
    }

    @Test
    public void testGetMinimumValue() {
        Assert.assertEquals(1, dateTimeField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        Assert.assertEquals(12, dateTimeField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        long expected = new LocalDate(2019, 1, 1).toDate().getTime();
        Assert.assertEquals(expected, dateTimeField.roundFloor(n));
    }

    @Test
    public void testRemainder() {
        long n = new LocalDate(2019, 1, 31).toDate().getTime();
        Assert.assertEquals(30, dateTimeField.remainder(n));
    }
}