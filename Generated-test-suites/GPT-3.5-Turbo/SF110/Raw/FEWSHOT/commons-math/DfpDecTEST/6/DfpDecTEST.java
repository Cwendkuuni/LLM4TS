package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {
    
    @Test
    public void testDfpDecConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(0, dfp.intValue());
        assertEquals(0.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        assertEquals(1, dfp.intValue());
        assertEquals(1.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 10);
        assertEquals(10, dfp.intValue());
        assertEquals(10.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 100000000000000L);
        assertEquals(100000000000000L, dfp.longValue());
        assertEquals(100000000000000.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.234);
        assertEquals(1.234, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor6() {
        DfpDec dfp = new DfpDec(new DfpField(), new Dfp(10));
        assertEquals(10, dfp.intValue());
        assertEquals(10.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "10");
        assertEquals(10, dfp.intValue());
        assertEquals(10.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 3);
        assertEquals(1, dfp.intValue());
        assertTrue(dfp.isNaN());
    }
    
    @Test
    public void testDfpDecNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance();
        assertEquals(0, dfp.intValue());
        assertEquals(0.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1);
        assertEquals(1, dfp.intValue());
        assertEquals(1.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(10);
        assertEquals(10, dfp.intValue());
        assertEquals(10.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(100000000000000L);
        assertEquals(100000000000000L, dfp.longValue());
        assertEquals(100000000000000.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1.234);
        assertEquals(1.234, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance6() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(new Dfp(10));
        assertEquals(10, dfp.intValue());
        assertEquals(10.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance("10");
        assertEquals(10, dfp.intValue());
        assertEquals(10.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 3);
        assertEquals(1, dfp.intValue());
        assertTrue(dfp.isNaN());
    }
    
    @Test
    public void testDfpDecGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(13, dfp.getDecimalDigits());
    }
    
    @Test
    public void testDfpDecRound() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.234);
        assertEquals(1, dfp.round());
        assertEquals(1, dfp.intValue());
        assertEquals(1.0, dfp.doubleValue(), 0.000001);
    }
    
    @Test
    public void testDfpDecNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.234);
        DfpDec next = dfp.nextAfter(new DfpDec(new DfpField(), 1.2345));
        assertEquals(1.2345, next.doubleValue(), 0.000001);
    }
}