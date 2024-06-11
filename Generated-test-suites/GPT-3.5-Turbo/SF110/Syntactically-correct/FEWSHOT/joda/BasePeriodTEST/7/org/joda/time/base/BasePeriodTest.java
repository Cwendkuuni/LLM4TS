package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import org.joda.time.field.BasePeriodField;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.*;

import org.junit.Test;

public class BasePeriodTest {
    
    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1);
        assertEquals(1, period.getValue(0));
    }
    
    @Test
    public void testToDurationFrom() {
        DateTime dateTime = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        BasePeriod period = new BasePeriod(1);
        Duration duration = period.toDurationFrom(dateTime);
        assertEquals(1, duration.getMillis());
    }
    
    @Test
    public void testToDurationTo() {
        DateTime dateTime = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        BasePeriod period = new BasePeriod(1);
        Duration duration = period.toDurationTo(dateTime);
        assertEquals(-1, duration.getMillis());
    }
    
    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1);
        period.setField(DurationFieldType.years(), 2);
        assertEquals(2, period.getValue(0));
    }
    
    @Test
    public void testSetFieldInto() {
        BasePeriod period = new BasePeriod(1);
        int[] values = {1, 2};
        period.setFieldInto(values, DurationFieldType.years(), 3);
        assertEquals(3, values[0]);
    }
    
    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1);
        period.addField(DurationFieldType.years(), 2);
        assertEquals(3, period.getValue(0));
    }
    
    @Test
    public void testAddFieldInto() {
        BasePeriod period = new BasePeriod(1);
        int[] values = {1, 2};
        period.addFieldInto(values, DurationFieldType.years(), 3);
        assertEquals(4, values[0]);
    }
    
    @Test
    public void testMergePeriod() {
        BasePeriod period1 = new BasePeriod(1);
        BasePeriod period2 = new BasePeriod(2);
        period1.mergePeriod(period2);
        assertEquals(3, period1.getValue(0));
    }
    
    @Test
    public void testMergePeriodInto() {
        BasePeriod period = new BasePeriod(1);
        int[] values = {1, 2};
        int[] mergedValues = period.mergePeriodInto(values, new BasePeriod(2));
        assertEquals(3, mergedValues[0]);
    }
    
    @Test
    public void testAddPeriod() {
        BasePeriod period1 = new BasePeriod(1);
        BasePeriod period2 = new BasePeriod(2);
        period1.addPeriod(period2);
        assertEquals(3, period1.getValue(0));
    }
    
    @Test
    public void testAddPeriodInto() {
        BasePeriod period = new BasePeriod(1);
        int[] values = {1, 2};
        int[] addedValues = period.addPeriodInto(values, new BasePeriod(2));
        assertEquals(3, addedValues[0]);
    }
    
    @Test
    public void testSetValue() {
        BasePeriod period = new BasePeriod(1);
        period.setValue(0, 2);
        assertEquals(2, period.getValue(0));
    }
    
    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(1);
        int[] values = {2};
        period.setValues(values);
        assertEquals(2, period.getValue(0));
    }
}