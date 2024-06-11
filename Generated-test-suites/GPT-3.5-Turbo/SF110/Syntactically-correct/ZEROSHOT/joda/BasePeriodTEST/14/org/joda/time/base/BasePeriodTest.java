package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PeriodConverter;
import org.joda.time.field.FieldUtils;
import org.junit.*;
import static org.junit.Assert.*;

public class BasePeriodTest {
    
    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(7, period.getValue(6));
        assertEquals(8, period.getValue(7));
    }
    
    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        period.setField(DurationFieldType.years(), 10);
        assertEquals(10, period.getValue(0));
    }
    
    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        period.addField(DurationFieldType.years(), 10);
        assertEquals(11, period.getValue(0));
    }
    
    @Test
    public void testMergePeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        BasePeriod otherPeriod = new BasePeriod(10, 20, 30, 40, 50, 60, 70, 80, PeriodType.time());
        period.mergePeriod(otherPeriod);
        assertEquals(11, period.getValue(0));
        assertEquals(22, period.getValue(1));
        assertEquals(33, period.getValue(2));
        assertEquals(44, period.getValue(3));
        assertEquals(55, period.getValue(4));
        assertEquals(66, period.getValue(5));
        assertEquals(77, period.getValue(6));
        assertEquals(88, period.getValue(7));
    }
    
    @Test
    public void testAddPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        BasePeriod otherPeriod = new BasePeriod(10, 20, 30, 40, 50, 60, 70, 80, PeriodType.time());
        period.addPeriod(otherPeriod);
        assertEquals(11, period.getValue(0));
        assertEquals(22, period.getValue(1));
        assertEquals(33, period.getValue(2));
        assertEquals(44, period.getValue(3));
        assertEquals(55, period.getValue(4));
        assertEquals(66, period.getValue(5));
        assertEquals(77, period.getValue(6));
        assertEquals(88, period.getValue(7));
    }
    
    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        DateTime instant = DateTime.now();
        Duration duration = period.toDurationFrom(instant);
        assertNotNull(duration);
    }
    
    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.time());
        DateTime instant = DateTime.now();
        Duration duration = period.toDurationTo(instant);
        assertNotNull(duration);
    }
}