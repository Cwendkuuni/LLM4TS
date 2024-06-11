import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(null, 0);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = 1500000000L;
        int expected = 8;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        long instant = 1500000000L;
        int months = 2;
        long expected = 1555200000L;
        long actual = field.add(instant, months);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        long instant = 1500000000L;
        int months = 2;
        long expected = 1555200000L;
        long actual = field.addWrapField(instant, months);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 1500000000L;
        long subtrahendInstant = 1490000000L;
        long expected = 11;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        long instant = 1500000000L;
        int month = 12;
        long expected = 1546214400L;
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        long instant = 1500000000L;
        boolean expected = false;
        boolean actual = field.isLeap(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = 1500000000L;
        int expected = 0;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        int expected = 1;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = Integer.MAX_VALUE;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long instant = 1500000000L;
        long expected = 1498867200L;
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        long instant = 1500000000L;
        long expected = 1132800L;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }

}