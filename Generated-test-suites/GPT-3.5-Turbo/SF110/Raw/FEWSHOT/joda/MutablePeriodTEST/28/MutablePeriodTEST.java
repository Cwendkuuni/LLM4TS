package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.base.*;
import java.io.*;
import org.joda.convert.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String s = "P1Y2M3DT4H5M6S";
        MutablePeriod expected = new MutablePeriod(1, 2, 0, 3, 4, 5, 6, 0);
        MutablePeriod actual = MutablePeriod.parse(s);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithFormatter() {
        String s = "1 year, 2 months, 3 days, 4 hours, 5 minutes, 6 seconds";
        PeriodFormatter formatter = PeriodFormatterBuilder()
            .appendYears().appendSuffix(" year", " years").appendSeparator(", ")
            .appendMonths().appendSuffix(" month", " months").appendSeparator(", ")
            .appendDays().appendSuffix(" day", " days").appendSeparator(", ")
            .appendHours().appendSuffix(" hour", " hours").appendSeparator(", ")
            .appendMinutes().appendSuffix(" minute", " minutes").appendSeparator(", ")
            .appendSeconds().appendSuffix(" second", " seconds").toFormatter();
        MutablePeriod expected = new MutablePeriod(1, 2, 0, 3, 4, 5, 6, 0);
        MutablePeriod actual = MutablePeriod.parse(s, formatter);
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor() {
        MutablePeriod period = new MutablePeriod();
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
    public void testConstructorWithPeriodType() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod period = new MutablePeriod(periodType);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    // Add more test cases for other constructors and methods in MutablePeriod class

}