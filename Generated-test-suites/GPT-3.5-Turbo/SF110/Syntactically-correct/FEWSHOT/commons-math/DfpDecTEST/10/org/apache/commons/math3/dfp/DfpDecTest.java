package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDfpDecConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(dfp, dfp.newInstance());
    }

    @Test
    public void testDfpDecConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        assertEquals(dfp, dfp.newInstance((byte) 1));
    }

    @Test
    public void testDfpDecConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        assertEquals(dfp, dfp.newInstance(1));
    }

    @Test
    public void testDfpDecConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        assertEquals(dfp, dfp.newInstance(1L));
    }

    @Test
    public void testDfpDecConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        assertEquals(dfp, dfp.newInstance(1.0));
    }

    @Test
    public void testDfpDecConstructor6() {
        Dfp d = new Dfp(new DfpField());
        DfpDec dfp = new DfpDec(d);
        assertEquals(dfp, dfp.newInstance(d));
    }

    @Test
    public void testDfpDecConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "1");
        assertEquals(dfp, dfp.newInstance("1"));
    }

    @Test
    public void testDfpDecConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        assertEquals(dfp, dfp.newInstance((byte) 1, (byte) 1));
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(20, dfp.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int result = dfp.round(0);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec x = dfp.newInstance();
        assertEquals(dfp, dfp.nextAfter(x));
    }
}