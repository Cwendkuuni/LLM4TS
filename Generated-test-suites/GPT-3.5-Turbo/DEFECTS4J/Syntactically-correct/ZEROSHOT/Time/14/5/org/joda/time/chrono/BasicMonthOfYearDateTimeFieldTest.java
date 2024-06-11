import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 1);
    }

    @Test
    public void testGet() {
        long instant = 1500000000L;
        int expected = 9;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLenient() {
        boolean actual = field.isLenient();
        assertFalse(actual);
    }

    @Test
    public void testAdd() {
        long instant = 1500000000L;
        int monthsToAdd = 2;
        long expected = 1830000000L;
        long actual = field.add(instant, monthsToAdd);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        long instant = 1500000000L;
        int monthsToAdd = 12;
        long expected = 1500000000L;
        long actual = field.addWrapField(instant, monthsToAdd);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 2000000000L;
        long subtrahendInstant = 1500000000L;
        long expected = 3;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        long instant = 1500000000L;
        int month = 6;
        long expected = 1830000000L;
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        long instant = 1500000000L;
        boolean actual = field.isLeap(instant);
        assertFalse(actual);
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
        int expected = 12;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long instant = 1500000000L;
        long expected = 1500000000L;
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        long instant = 1500000000L;
        long expected = 0L;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }
}