import static org.junit.Assert.*;

import org.joda.time.MutablePeriod;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.Period;


import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod expected = new MutablePeriod();
        MutablePeriod result = MutablePeriod.parse("P0Y0M0W0DT0H0M0S");
        assertEquals(expected, result);
    }

    @Test
    public void testMutablePeriod() {
        MutablePeriod expected = new MutablePeriod(0, 0, 0, 0);
        MutablePeriod result = new MutablePeriod();
        assertEquals(expected, result);
    }

    @Test
    public void testClear() {
        MutablePeriod mutablePeriod = new MutablePeriod(1, 1, 1, 1);
        mutablePeriod.clear();
        assertEquals(new MutablePeriod(), mutablePeriod);
    }

    @Test
    public void testSetValue() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setValue(Period.years(), 2);
        assertEquals(2, mutablePeriod.getYears());
    }

    // Add more test cases for other methods

}