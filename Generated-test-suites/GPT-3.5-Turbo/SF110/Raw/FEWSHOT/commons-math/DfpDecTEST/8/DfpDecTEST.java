package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDfpDec() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        assertEquals(field, dfpDec.getField());
    }
    
    @Test
    public void testDfpDecWithByte() {
        DfpField field = new DfpField();
        byte x = 1;
        DfpDec dfpDec = new DfpDec(field, x);
        assertEquals(x, dfpDec.getInternalValue()[0]);
    }
    
    @Test
    public void testDfpDecWithInt() {
        DfpField field = new DfpField();
        int x = 1;
        DfpDec dfpDec = new DfpDec(field, x);
        assertEquals(x, dfpDec.getInternalValue()[0]);
    }
    
    @Test
    public void testDfpDecWithLong() {
        DfpField field = new DfpField();
        long x = 1L;
        DfpDec dfpDec = new DfpDec(field, x);
        assertEquals(x, dfpDec.getInternalValue()[0]);
    }
    
    @Test
    public void testDfpDecWithDouble() {
        DfpField field = new DfpField();
        double x = 1.0;
        DfpDec dfpDec = new DfpDec(field, x);
        assertEquals(x, dfpDec.toDouble(), 0);
    }
    
    @Test
    public void testDfpDecWithDfp() {
        DfpField field = new DfpField();
        Dfp d = new Dfp(field);
        DfpDec dfpDec = new DfpDec(d);
        assertEquals(d, dfpDec.getDfp());
    }
    
    @Test
    public void testDfpDecWithString() {
        DfpField field = new DfpField();
        String s = "1";
        DfpDec dfpDec = new DfpDec(field, s);
        assertEquals(s, dfpDec.toString());
    }
    
    @Test
    public void testDfpDecWithSignAndNans() {
        DfpField field = new DfpField();
        byte sign = 1;
        byte nans = 2;
        DfpDec dfpDec = new DfpDec(field, sign, nans);
        assertEquals(sign, dfpDec.getSign());
        assertEquals(nans, dfpDec.getNans());
    }
    
    @Test
    public void testNewInstance() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance();
        assertEquals(field, newInstance.getField());
    }
    
    @Test
    public void testNewInstanceWithByte() {
        DfpField field = new DfpField();
        byte x = 1;
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(x);
        assertEquals(x, newInstance.getInternalValue()[0]);
    }
    
    @Test
    public void testNewInstanceWithInt() {
        DfpField field = new DfpField();
        int x = 1;
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(x);
        assertEquals(x, newInstance.getInternalValue()[0]);
    }
    
    @Test
    public void testNewInstanceWithLong() {
        DfpField field = new DfpField();
        long x = 1L;
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(x);
        assertEquals(x, newInstance.getInternalValue()[0]);
    }
    
    @Test
    public void testNewInstanceWithDouble() {
        DfpField field = new DfpField();
        double x = 1.0;
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(x);
        assertEquals(x, newInstance.toDouble(), 0);
    }
    
    @Test
    public void testNewInstanceWithDfp() {
        DfpField field = new DfpField();
        Dfp d = new Dfp(field);
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(d);
        assertEquals(d, newInstance.getDfp());
    }
    
    @Test
    public void testNewInstanceWithString() {
        DfpField field = new DfpField();
        String s = "1";
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(s);
        assertEquals(s, newInstance.toString());
    }
    
    @Test
    public void testNewInstanceWithSignAndNans() {
        DfpField field = new DfpField();
        byte sign = 1;
        byte nans = 2;
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance(sign, nans);
        assertEquals(sign, newInstance.getSign());
        assertEquals(nans, newInstance.getNans());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int expected = dfpDec.getRadixDigits() * 4 - 3;
        assertEquals(expected, dfpDec.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1.0);
        int in = 0;
        int expected = 0;
        int actual = dfpDec.round(in);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRoundWithNonZeroMsb() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 10.0);
        int in = 0;
        int expected = 16;
        int actual = dfpDec.round(in);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNextAfter() {
        DfpField field = new DfpField();
        DfpDec x = new DfpDec(field, 1.0);
        DfpDec result = x.nextAfter(x);
        assertEquals(x, result);
    }
    
    @Test
    public void testNextAfterWithEqualValues() {
        DfpField field = new DfpField();
        DfpDec x = new DfpDec(field, 1.0);
        DfpDec result = x.nextAfter(x);
        assertEquals(x, result);
    }
    
    @Test
    public void testNextAfterWithNegativeValues() {
        DfpField field = new DfpField();
        DfpDec x = new DfpDec(field, -1.0);
        DfpDec result = x.nextAfter(x);
        assertEquals(x, result);
    }
}