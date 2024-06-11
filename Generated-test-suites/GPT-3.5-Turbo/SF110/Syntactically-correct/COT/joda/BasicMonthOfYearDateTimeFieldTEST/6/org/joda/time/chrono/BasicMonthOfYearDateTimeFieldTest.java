import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int expected = 0;
        int actual = field.get(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddIntZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 0;
        long expected = 0;
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLongZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long n2 = 0;
        long expected = 0;
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddIntPositive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 1;
        long expected = 1;
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddIntNegative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = -1;
        long expected = -1;
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLongPositive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long n2 = 1L;
        long expected = 1L;
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLongNegative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long n2 = -1L;
        long expected = -1L;
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLongOutOfRange() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long n2 = Integer.MAX_VALUE;
        field.add(n, n2);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 1;
        long expected = 1;
        long actual = field.addWrapField(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n1 = 0;
        long n2 = 1;
        long expected = -1;
        long actual = field.getDifferenceAsLong(n1, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 1;
        long expected = 1;
        long actual = field.set(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertNotNull(field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        assertFalse(field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int expected = 0;
        int actual = field.getLeapAmount(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertNotNull(field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int expected = 1;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int expected = 0;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long expected = 0;
        long actual = field.roundFloor(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long expected = 0;
        long actual = field.remainder(n);
        assertEquals(expected, actual);
    }

}
