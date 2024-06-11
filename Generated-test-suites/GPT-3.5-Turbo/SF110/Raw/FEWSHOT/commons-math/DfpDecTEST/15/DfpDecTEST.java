package org.apache.commons.math3.dfp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DfpDecTest {
    
    @Test
    public void testDfpDecConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), (int) 1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), (long) 1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), (double) 1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor6() {
        DfpDec dfp = new DfpDec(new Dfp(1));
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "1");
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance();
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte) 1);
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((int) 1);
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((long) 1);
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((double) 1);
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance6() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp d = new Dfp(1);
        Dfp result = dfp.newInstance(d);
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance("1");
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte) 1, (byte) 1);
        assertEquals(Dfp.class, result.getClass());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int digits = dfp.getDecimalDigits();
        assertEquals(int.class, digits.getClass());
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int round = dfp.round(0);
        assertEquals(int.class, round.getClass());
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp d = new Dfp(1);
        Dfp result = dfp.nextAfter(d);
        assertEquals(Dfp.class, result.getClass());
    }
}