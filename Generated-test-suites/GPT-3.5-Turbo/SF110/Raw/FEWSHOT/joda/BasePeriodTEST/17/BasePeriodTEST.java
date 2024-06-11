
package org.joda.time.base;

import static org.junit.Assert.assertEquals;

import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        
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
    public void testToDurationFrom() {
        ReadableInstant from = null;
        BasePeriod period = new BasePeriod(1);
        assertEquals(null, period.toDurationFrom(from));
    }
    
    @Test
    public void testToDurationTo() {
        ReadableInstant to = null;
        BasePeriod period = new BasePeriod(1);
        assertEquals(null, period.toDurationTo(to));
    }
    
    @Test
    public void testSetField() {
        DurationFieldType fieldType = null;
        int value = 0;
        BasePeriod period = new BasePeriod(1);
        period.setField(fieldType, value);
    }
    
    @Test
    public void testAddField() {
        DurationFieldType fieldType = null;
        int value = 0;
        BasePeriod period = new BasePeriod(1);
        period.addField(fieldType, value);
    }
    
    @Test
    public void testMergePeriod() {
        ReadablePeriod readablePeriod = null;
        BasePeriod period = new BasePeriod(1);
        period.mergePeriod(readablePeriod);
    }
    
    @Test
    public void testAddPeriod() {
        ReadablePeriod readablePeriod = null;
        BasePeriod period = new BasePeriod(1);
        period.addPeriod(readablePeriod);
    }
    
    @Test
    public void testSetValue() {
        int index = 0;
        int value = 0;
        BasePeriod period = new BasePeriod(1);
        period.setValue(index, value);
    }
    
    @Test
    public void testSetValues() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(1);
        period.setValues(array);
    }
}
