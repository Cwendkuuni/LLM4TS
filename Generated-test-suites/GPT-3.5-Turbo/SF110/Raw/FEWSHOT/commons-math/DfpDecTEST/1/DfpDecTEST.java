package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {
    DfpDec instance = new DfpDec(new DfpField());

    @Test
    public void testConstructor() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertNull(dfpDec.getField());
        assertEquals(0, dfpDec.getRadixDigits());
    }

    @Test
    public void testConstructorWithByte() {
        byte x = 1;
        DfpDec dfpDec = new DfpDec(new DfpField(), x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
    }

    @Test
    public void testConstructorWithInt() {
        int x = 1;
        DfpDec dfpDec = new DfpDec(new DfpField(), x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
    }

    @Test
    public void testConstructorWithLong() {
        long x = 1L;
        DfpDec dfpDec = new DfpDec(new DfpField(), x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
    }

    @Test
    public void testConstructorWithDouble() {
        double x = 1.0;
        DfpDec dfpDec = new DfpDec(new DfpField(), x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
        assertEquals(0, dfpDec.exp);
    }

    @Test
    public void testConstructorWithDfp() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfpDec = new DfpDec(d);
        assertNull(dfpDec.getField());
        assertEquals(d.mant.length, dfpDec.mant.length);
        assertEquals(d.exp, dfpDec.exp);
    }

    @Test
    public void testConstructorWithString() {
        String s = "1.0";
        DfpDec dfpDec = new DfpDec(new DfpField(), s);
        assertNull(dfpDec.getField());
        assertEquals(s.length(), dfpDec.mant.length);
        assertEquals(0, dfpDec.exp);
    }

    @Test
    public void testConstructorWithByteAndByte() {
        byte sign = 1;
        byte nans = 1;
        DfpDec dfpDec = new DfpDec(new DfpField(), sign, nans);
        assertNull(dfpDec.getField());
        assertEquals(sign, dfpDec.sign);
        assertEquals(nans, dfpDec.nans);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = instance.newInstance();
        assertNull(dfpDec.getField());
    }

    @Test
    public void testNewInstanceWithByte() {
        byte x = 1;
        DfpDec dfpDec = instance.newInstance(x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
    }

    @Test
    public void testNewInstanceWithInt() {
        int x = 1;
        DfpDec dfpDec = instance.newInstance(x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
    }

    @Test
    public void testNewInstanceWithLong() {
        long x = 1L;
        DfpDec dfpDec = instance.newInstance(x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
    }

    @Test
    public void testNewInstanceWithDouble() {
        double x = 1.0;
        DfpDec dfpDec = instance.newInstance(x);
        assertNull(dfpDec.getField());
        assertEquals(x, dfpDec.mant.length);
        assertEquals(0, dfpDec.exp);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec d = instance.newInstance();
        DfpDec dfpDec = instance.newInstance(d);
        assertNull(dfpDec.getField());
        assertEquals(d.mant.length, dfpDec.mant.length);
        assertEquals(d.exp, dfpDec.exp);
    }

    @Test
    public void testNewInstanceWithString() {
        String s = "1.0";
        DfpDec dfpDec = instance.newInstance(s);
        assertNull(dfpDec.getField());
        assertEquals(s.length(), dfpDec.mant.length);
        assertEquals(0, dfpDec.exp);
    }

    @Test
    public void testNewInstanceWithByteAndByte() {
        byte sign = 1;
        byte nans = 1;
        DfpDec dfpDec = instance.newInstance(sign, nans);
        assertNull(dfpDec.getField());
        assertEquals(sign, dfpDec.sign);
        assertEquals(nans, dfpDec.nans);
    }

    @Test
    public void testGetDecimalDigits() {
        int expected = instance.getRadixDigits() * 4 - 3;
        int actual = instance.getDecimalDigits();
        assertEquals(expected, actual);
    }

    @Test
    public void testRound() {
        int in = 0;
        int expected = 0;
        int actual = instance.round(in);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextAfter() {
        DfpDec x = instance.newInstance();
        DfpDec result = instance.nextAfter(x);
        assertNull(result.getField());
        assertEquals(x.mant.length, result.mant.length);
        assertEquals(x.exp, result.exp);
    }
}