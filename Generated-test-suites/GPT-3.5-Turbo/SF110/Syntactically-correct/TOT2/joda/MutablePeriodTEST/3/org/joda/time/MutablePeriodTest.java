package org.joda.time;
import static org.junit.Assert.*;
import org.joda.time.MutablePeriod;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParseWithStandardFormatter() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
    }

    @Test
    public void testConstructorWithValues() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
    }

    // Add more test cases for other methods...
}