package org.joda.time.base;

import org.joda.time.field.DurationFieldType;
import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BasePeriodTest {
    
    @Test
    public void testGetPeriodType() {
        BasePeriod basePeriod = new BasePeriod(1);
        PeriodType periodType = PeriodType.standard();
        assertEquals(periodType, basePeriod.getPeriodType());
    }
    
    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(1);
        int value = 1;
        assertEquals(value, basePeriod.getValue(0));
    }
    
    @Test
    public void testToDurationFrom() {
        BasePeriod basePeriod = new BasePeriod(1);
        DateTime dateTime = new DateTime();
        Duration duration = basePeriod.toDurationFrom(dateTime);
        assertEquals(duration, new Duration(dateTime.getMillis(), dateTime.getChronology().add(basePeriod, dateTime.getMillis(), 1)));
    }
    
    @Test
    public void testToDurationTo() {
        BasePeriod basePeriod = new BasePeriod(1);
        DateTime dateTime = new DateTime();
        Duration duration = basePeriod.toDurationTo(dateTime);
        assertEquals(duration, new Duration(dateTime.getChronology().add(basePeriod, dateTime.getMillis(), -1), dateTime.getMillis()));
    }
    
    @Test
    public void testSetField() {
        BasePeriod basePeriod = new BasePeriod(1);
        basePeriod.setField(DurationFieldType.years(), 2);
        assertEquals(2, basePeriod.getValue(basePeriod.indexOf(DurationFieldType.years())));
    }
    
    @Test
    public void testAddField() {
        BasePeriod basePeriod = new BasePeriod(1);
        basePeriod.addField(DurationFieldType.years(), 2);
        assertEquals(3, basePeriod.getValue(basePeriod.indexOf(DurationFieldType.years())));
    }
    
    @Test
    public void testMergePeriod() {
        BasePeriod basePeriod = new BasePeriod(1);
        BasePeriod otherPeriod = new BasePeriod(1);
        otherPeriod.setValue(basePeriod.indexOf(DurationFieldType.years()), 2);
        basePeriod.mergePeriod(otherPeriod);
        assertEquals(2, basePeriod.getValue(basePeriod.indexOf(DurationFieldType.years())));
    }
    
    @Test
    public void testAddPeriod() {
        BasePeriod basePeriod = new BasePeriod(1);
        BasePeriod otherPeriod = new BasePeriod(1);
        otherPeriod.setValue(basePeriod.indexOf(DurationFieldType.years()), 2);
        basePeriod.addPeriod(otherPeriod);
        assertEquals(3, basePeriod.getValue(basePeriod.indexOf(DurationFieldType.years())));
    }
    
    @Test
    public void testSetValue() {
        BasePeriod basePeriod = new BasePeriod(1);
        basePeriod.setValue(basePeriod.indexOf(DurationFieldType.years()), 2);
        assertEquals(2, basePeriod.getValue(basePeriod.indexOf(DurationFieldType.years())));
    }
    
    @Test
    public void testSetValues() {
        BasePeriod basePeriod = new BasePeriod(1);
        int[] values = new int[basePeriod.size()];
        values[basePeriod.indexOf(DurationFieldType.years())] = 2;
        basePeriod.setValues(values);
        assertEquals(2, basePeriod.getValue(basePeriod.indexOf(DurationFieldType.years())));
    }
   
}