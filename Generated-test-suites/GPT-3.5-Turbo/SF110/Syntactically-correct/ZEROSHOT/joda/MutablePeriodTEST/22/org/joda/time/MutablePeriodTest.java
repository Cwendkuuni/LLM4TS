import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4DT5H6M7.8S");
        assertEquals(period.getYears(), 1);
        assertEquals(period.getMonths(), 2);
        assertEquals(period.getWeeks(), 3);
        assertEquals(period.getDays(), 4);
        assertEquals(period.getHours(), 5);
        assertEquals(period.getMinutes(), 6);
        assertEquals(period.getSeconds(), 7);
        assertEquals(period.getMillis(), 800);
    }

    @Test
    public void testConstructor1() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(period.getYears(), 0);
        assertEquals(period.getMonths(), 0);
        assertEquals(period.getWeeks(), 0);
        assertEquals(period.getDays(), 0);
        assertEquals(period.getHours(), 0);
        assertEquals(period.getMinutes(), 0);
        assertEquals(period.getSeconds(), 0);
        assertEquals(period.getMillis(), 0);
    }

    @Test
    public void testConstructor2() {
        MutablePeriod period = new MutablePeriod(PeriodType.standard());
        assertEquals(period.getYears(), 0);
        assertEquals(period.getMonths(), 0);
        assertEquals(period.getWeeks(), 0);
        assertEquals(period.getDays(), 0);
        assertEquals(period.getHours(), 0);
        assertEquals(period.getMinutes(), 0);
        assertEquals(period.getSeconds(), 0);
        assertEquals(period.getMillis(), 0);
    }

    @Test
    public void testConstructor3() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertEquals(period.getYears(), 0);
        assertEquals(period.getMonths(), 0);
        assertEquals(period.getWeeks(), 0);
        assertEquals(period.getDays(), 0);
        assertEquals(period.getHours(), 1);
        assertEquals(period.getMinutes(), 2);
        assertEquals(period.getSeconds(), 3);
        assertEquals(period.getMillis(), 4);
    }

    // Add more test methods to cover all the methods in MutablePeriod class

}