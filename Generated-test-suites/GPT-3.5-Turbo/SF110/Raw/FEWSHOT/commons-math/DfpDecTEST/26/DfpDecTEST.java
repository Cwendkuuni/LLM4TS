package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    @Test
    public void testDfpDecConstructor() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithByte() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithInt() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithLong() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1L);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1.0);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithDfp() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        DfpDec dfpDec = new DfpDec(dfp);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithString() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "1");
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructorWithSignAndNans() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) -1, (byte) 3);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance();
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance((byte) 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1L);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1.0);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(new DfpDec(new DfpField(), 1.0));
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithString() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance("1");
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceWithSignAndNans() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance((byte) -1, (byte) 3);
        assertNotNull(dfpDec);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(0, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int rounded = dfpDec.round(0);
        assertEquals(0, rounded);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(new DfpField(), 1.0);
        DfpDec dfpDec2 = new DfpDec(new DfpField(), 2.0);
        DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
        assertNotNull(nextAfter);
    }
}