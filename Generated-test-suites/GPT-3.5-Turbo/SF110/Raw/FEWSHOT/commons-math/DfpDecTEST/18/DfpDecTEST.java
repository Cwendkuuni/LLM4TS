package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(0, dfp.getExp());
        assertEquals("0", dfp.toString());
    }
    
    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(10), (byte) 1);
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("1", dfp.toString());
    }
    
    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(10), 1);
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("1", dfp.toString());
    }
    
    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(10), 1L);
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("1", dfp.toString());
    }
    
    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(10), 1.0);
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("1.0", dfp.toString());
    }
    
    @Test
    public void testConstructor6() {
        Dfp d = new Dfp(new DfpField(10), 1);
        DfpDec dfp = new DfpDec(d);
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("1", dfp.toString());
    }
    
    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(10), "1");
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("1", dfp.toString());
    }
    
    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(10), (byte) 1, (byte) 1);
        assertEquals(10, dfp.getField().getRadixDigits());
        assertEquals(1, dfp.getExp());
        assertEquals("NaN", dfp.toString());
    }
    
    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance();
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(0, result.getExp());
        assertEquals("0", result.toString());
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance((byte) 1);
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("0.1", result.toString());
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance(1);
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("1", result.toString());
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance(1L);
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("1", result.toString());
    }
    
    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance(1.0);
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("1.0", result.toString());
    }
    
    @Test
    public void testNewInstance6() {
        Dfp dfp = new Dfp(new DfpField(10), 1);
        Dfp result = dfp.newInstance();
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(0, result.getExp());
        assertEquals("0", result.toString());
    }
    
    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance("1");
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("1", result.toString());
    }
    
    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp result = dfp.newInstance((byte) 1, (byte) 1);
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("NaN", result.toString());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        int digits = dfp.getDecimalDigits();
        assertEquals(37, digits);
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        int in = 1000;
        int result = dfp.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField(10));
        Dfp x = new Dfp(new DfpField(10), 1);
        Dfp result = dfp.nextAfter(x);
        assertEquals(10, result.getField().getRadixDigits());
        assertEquals(1, result.getExp());
        assertEquals("0.1", result.toString());
    }
}