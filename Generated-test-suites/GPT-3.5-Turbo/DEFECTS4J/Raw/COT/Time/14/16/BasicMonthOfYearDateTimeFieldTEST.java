import org.junit.Before;
import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

import static org.junit.Assert.*;



public class BasicMonthOfYearDateTimeFieldTest {
    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(null, 1);
    }

    @Test
    public void isLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void get() {
        assertEquals(1, field.get(0));
        assertEquals(12, field.get(1000000000));
    }

    @Test
    public void add() {
        assertEquals(10, field.add(0, 9));
        assertEquals(4, field.add(1000, -8));
    }

    @Test
    public void addWrapField() {
        assertEquals(1, field.addWrapField(12, 13));
        assertEquals(12, field.addWrapField(1, -2));
    }

    @Test
    public void getDifferenceAsLong() {
        assertEquals(-12000, field.getDifferenceAsLong(0, 1000));
        assertEquals(24000, field.getDifferenceAsLong(1000, 0));
    }

    @Test
    public void set() {
        assertEquals(1000, field.set(0, 12));
        assertEquals(1500, field.set(1000, 6));
    }

    @Test
    public void isLeap() {
        assertTrue(field.isLeap(0));
        assertFalse(field.isLeap(1000));
    }

    @Test
    public void getLeapAmount() {
        assertEquals(1, field.getLeapAmount(0));
        assertEquals(0, field.getLeapAmount(1000));
    }

    @Test
    public void getMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void getMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void roundFloor() {
        assertEquals(0, field.roundFloor(100));
        assertEquals(1000, field.roundFloor(1000));
    }

    @Test
    public void remainder() {
        assertEquals(100, field.remainder(1000));
        assertEquals(0, field.remainder(100));
    }
}

