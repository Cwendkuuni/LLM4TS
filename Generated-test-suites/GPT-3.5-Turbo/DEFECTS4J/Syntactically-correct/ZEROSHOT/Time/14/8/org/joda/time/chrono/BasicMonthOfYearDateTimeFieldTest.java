import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        int leapMonth = 2; // February is the leap month
        field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    }

    @Test
    public void isLenient_false() {
        assertFalse(field.isLenient());
    }

    @Test
    public void get() {
        long instant = 1577836800000L; // 2020-01-01
        assertEquals(1, field.get(instant));
    }

    @Test
    public void add_zeroMonths() {
        long instant = 1577836800000L; // 2020-01-01
        assertEquals(instant, field.add(instant, 0));
    }

    @Test
    public void add_positiveMonths() {
        long instant = 1577836800000L; // 2020-01-01
        long expected = 1580515200000L; // 2020-02-01
        assertEquals(expected, field.add(instant, 1));
    }

    @Test
    public void add_negativeMonths() {
        long instant = 1577836800000L; // 2020-01-01
        long expected = 1575220800000L; // 2019-12-01
        assertEquals(expected, field.add(instant, -1));
    }

    @Test
    public void addWrapField() {
        long instant = 1577836800000L; // 2020-01-01
        long expected = 1577836800000L; // 2020-01-01 (no wrapping)
        assertEquals(expected, field.addWrapField(instant, 1));
    }

    @Test
    public void getDifferenceAsLong() {
        long minuendInstant = 1577836800000L; // 2020-01-01
        long subtrahendInstant = 1575220800000L; // 2019-12-01
        assertEquals(1, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void set() {
        long instant = 1577836800000L; // 2020-01-01
        long expected = 1580515200000L; // 2020-02-01
        assertEquals(expected, field.set(instant, 2));
    }

    @Test
    public void isLeap_true() {
        long instant = 1577836800000L; // 2020-01-01
        assertTrue(field.isLeap(instant));
    }

    @Test
    public void isLeap_false() {
        long instant = 1577836800000L; // 2020-01-01
        assertFalse(field.isLeap(instant - 86400000)); // 2019-12-31
    }

    @Test
    public void getLeapAmount() {
        long instant = 1577836800000L; // 2020-01-01
        assertEquals(1, field.getLeapAmount(instant));
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
        long instant = 1577836800000L; // 2020-01-01
        long expected = 1577836800000L; // 2020-01-01 (no rounding)
        assertEquals(expected, field.roundFloor(instant));
    }

    @Test
    public void remainder() {
        long instant = 1577836800000L; // 2020-01-01
        assertEquals(0, field.remainder(instant));
    }
}