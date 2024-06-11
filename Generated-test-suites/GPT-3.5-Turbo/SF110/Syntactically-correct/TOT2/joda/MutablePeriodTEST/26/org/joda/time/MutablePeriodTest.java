package org.joda.time;
import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P2Y3M");
        assertNotNull(period);
        assertEquals(2, period.getYears());
        assertEquals(3, period.getMonths());
    }

    @Test
    public void testConstructor() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        assertNotNull(period);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
    }

    // Add more individual test cases for each method as needed

}