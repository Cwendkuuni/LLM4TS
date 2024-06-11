package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(PeriodType.days(), period.getPeriodType());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new TestSingleFieldPeriod(10);
        
        assertTrue(period1.compareTo(period2) > 0);
        assertTrue(period2.compareTo(period1) < 0);
        assertTrue(period1.compareTo(period3) == 0);
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod period4 = new TestSingleFieldPeriod(5);
        
        assertFalse(period1.equals(period2));
        assertFalse(period2.equals(period1));
        assertTrue(period1.equals(period3));
        assertFalse(period1.equals(period4));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(361, period.hashCode());
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertFalse(period.isSupported(DurationFieldType.hours()));
        assertTrue(period.isSupported(DurationFieldType.days()));
    }

    // Extension of BaseSingleFieldPeriod for testing purposes
    private class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        
        public TestSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.days();
        }    
    }
}