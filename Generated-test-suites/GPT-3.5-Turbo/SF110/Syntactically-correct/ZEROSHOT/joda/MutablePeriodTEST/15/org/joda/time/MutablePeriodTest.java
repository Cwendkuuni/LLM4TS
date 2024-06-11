import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MutablePeriodTest {

    private MutablePeriod period;

    @Before
    public void setUp() {
        period = new MutablePeriod();
    }

    @Test
    public void testParse() {
        MutablePeriod parsedPeriod = MutablePeriod.parse("P1Y2M3D");
        assertEquals(1, parsedPeriod.getYears());
        assertEquals(2, parsedPeriod.getMonths());
        assertEquals(3, parsedPeriod.getDays());
    }

    @Test
    public void testSetValues() {
        period.setYears(1);
        period.setMonths(2);
        period.setWeeks(3);
        period.setDays(4);
        period.setHours(5);
        period.setMinutes(6);
        period.setSeconds(7);
        period.setMillis(8);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testSetField() {
        period.set(DurationFieldType.years(), 1);
        period.set(DurationFieldType.months(), 2);
        period.set(DurationFieldType.weeks(), 3);
        period.set(DurationFieldType.days(), 4);
        period.set(DurationFieldType.hours(), 5);
        period.set(DurationFieldType.minutes(), 6);
        period.set(DurationFieldType.seconds(), 7);
        period.set(DurationFieldType.millis(), 8);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testSetPeriod() {
        period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testSetPeriodWithNullInterval() {
        period.setPeriod((ReadableInterval) null);

        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetPeriodWithInterval() {
        period.setPeriod(new DateTime(), new DateTime().plusYears(1));

        assertEquals(1, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetPeriodWithMillis() {
        period.setPeriod(1000);

        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(1000, period.getMillis());
    }

    @Test
    public void testAddField() {
        period.add(DurationFieldType.years(), 1);
        period.add(DurationFieldType.months(), 2);
        period.add(DurationFieldType.weeks(), 3);
        period.add(DurationFieldType.days(), 4);
        period.add(DurationFieldType.hours(), 2);
        period.add(DurationFieldType.minutes(), 2);
        period.add(DurationFieldType.seconds(), 2);
        period.add(DurationFieldType.millis(), 2);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(2, period.getHours());
        assertEquals(2, period.getMinutes());
        assertEquals(2, period.getSeconds());
        assertEquals(2, period.getMillis());
    }

    @Test
    public void testAddPeriod() {
        MutablePeriod otherPeriod = new MutablePeriod();
        otherPeriod.setPeriod(1, 2, 3, 4, 2, 2, 2, 2);

        period.add(otherPeriod);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(2, period.getHours());
        assertEquals(2, period.getMinutes());
        assertEquals(2, period.getSeconds());
        assertEquals(2, period.getMillis());
    }

    @Test
    public void testAddPeriodWithInterval() {
        period.add(new DateTime(), new DateTime().plusYears(1));

        assertEquals(1, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testAddPeriodWithDuration() {
        period.add(new Duration(1000));

        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(1000, period.getMillis());
    }

    @Test
    public void testAddWithMillis() {
        period.add(1000);

        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(1000, period.getMillis());
    }

    @Test
    public void testCopy() {
        MutablePeriod copy = period.copy();

        assertEquals(period.getYears(), copy.getYears());
        assertEquals(period.getMonths(), copy.getMonths());
        assertEquals(period.getWeeks(), copy.getWeeks());
        assertEquals(period.getDays(), copy.getDays());
        assertEquals(period.getHours(), copy.getHours());
        assertEquals(period.getMinutes(), copy.getMinutes());
        assertEquals(period.getSeconds(), copy.getSeconds());
        assertEquals(period.getMillis(), copy.getMillis());
    }
}