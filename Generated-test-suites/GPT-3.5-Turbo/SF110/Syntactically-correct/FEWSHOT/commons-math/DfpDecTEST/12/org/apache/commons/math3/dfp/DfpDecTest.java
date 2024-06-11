package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDfpDecConstructor1() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor2() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor3() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor4() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1L);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor5() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1.0);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor6() {
        DfpDec dfpDec = new DfpDec(new Dfp(1));
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor7() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "1");
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor8() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance1() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance();
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance((byte) 1);
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance(1);
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance(1L);
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance5() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance(1.0);
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance6() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance(new Dfp(1));
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance7() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance("1");
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance8() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp dec = dfpDec.newInstance((byte) 1, (byte) 1);
        assertNotNull(dec);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(8, decimalDigits);
    }
    
    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int roundResult = dfpDec.round(0);
        assertEquals(0, roundResult);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.nextAfter(new Dfp(1));
        assertNotNull(result);
    }
}