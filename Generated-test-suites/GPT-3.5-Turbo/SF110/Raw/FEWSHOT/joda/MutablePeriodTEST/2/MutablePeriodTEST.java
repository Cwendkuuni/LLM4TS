package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        String s = "P1Y2M3DT4H5M6S";
        MutablePeriod expected = new MutablePeriod();
        expected.setYears(1);
        expected.setMonths(2);
        expected.setDays(3);
        expected.setHours(4);
        expected.setMinutes(5);
        expected.setSeconds(6);
        MutablePeriod actual = MutablePeriod.parse(s);
        
        assertEquals(expected.getYears(), actual.getYears());
        assertEquals(expected.getMonths(), actual.getMonths());
        assertEquals(expected.getDays(), actual.getDays());
        assertEquals(expected.getHours(), actual.getHours());
        assertEquals(expected.getMinutes(), actual.getMinutes());
        assertEquals(expected.getSeconds(), actual.getSeconds());
    }
    
    @Test
    public void testParseWithFormatter() {
        String s = "P1Y2M3DT4H5M6S";
        PeriodFormatter formatter = ISODateTimeFormat.yearMonthDayTime();
        MutablePeriod expected = new MutablePeriod();
        expected.setYears(1);
        expected.setMonths(2);
        expected.setDays(3);
        expected.setHours(4);
        expected.setMinutes(5);
        expected.setSeconds(6);
        MutablePeriod actual = MutablePeriod.parse(s, formatter);
        
        assertEquals(expected.getYears(), actual.getYears());
        assertEquals(expected.getMonths(), actual.getMonths());
        assertEquals(expected.getDays(), actual.getDays());
        assertEquals(expected.getHours(), actual.getHours());
        assertEquals(expected.getMinutes(), actual.getMinutes());
        assertEquals(expected.getSeconds(), actual.getSeconds());
    }
    
    @Test
    public void testConstructors() {
        MutablePeriod period = new MutablePeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        
        period = new MutablePeriod(1, 2, 3, 4);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        
        period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(5, period.getMinutes());
        assertEquals(6, period.getSeconds());
        
        PeriodType periodType = PeriodType.yearMonthDayHourMinuteSecond();
        period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, periodType);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(5, period.getMinutes());
        assertEquals(6, period.getSeconds());
    }
    
    @Test
    public void testSetters() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(1);
        period.setMonths(2);
        period.setDays(3);
        period.setHours(4);
        period.setMinutes(5);
        period.setSeconds(6);
        
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getDays());
        assertEquals(4, period.getHours());
        assertEquals(5, period.getMinutes());
        assertEquals(6, period.getSeconds());
    }
    
    @Test
    public void testAdd() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(1);
        period.setMonths(2);
        period.setDays(3);
        period.setHours(4);
        period.setMinutes(5);
        period.setSeconds(6);
        
        period.addYears(1);
        period.addMonths(2);
        period.addDays(3);
        period.addHours(4);
        period.addMinutes(5);
        period.addSeconds(6);
        
        assertEquals(2, period.getYears());
        assertEquals(4, period.getMonths());
        assertEquals(6, period.getDays());
        assertEquals(8, period.getHours());
        assertEquals(10, period.getMinutes());
        assertEquals(12, period.getSeconds());
    }
    
    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(1);
        period.setMonths(2);
        period.setDays(3);
        period.setHours(4);
        period.setMinutes(5);
        period.setSeconds(6);
        
        period.clear();
        
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
    }
}