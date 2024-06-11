package org.apache.commons.math3.dfp;

import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDefaultConstructor() {
        DfpDec dfp = new DfpDec(new DfpField());
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructorWithByte() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructorWithInt() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructorWithLong() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructorWithDouble() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructorWithDfp() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 1.0);
        DfpDec dfp2 = new DfpDec(dfp1);
        Assert.assertNotNull(dfp2);
    }

    @Test
    public void testConstructorWithString() {
        DfpDec dfp = new DfpDec(new DfpField(), "1.0");
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructorWithSignAndNans() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance();
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1L);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1.0);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 1.0);
        DfpDec dfp2 = new DfpDec(new DfpField()).newInstance(dfp1);
        Assert.assertNotNull(dfp2);
    }

    @Test
    public void testNewInstanceWithString() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance("1.0");
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithSignAndNans() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int digits = dfp.getDecimalDigits();
        // TODO: add assertion
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        int ret = dfp.round(0);
        // TODO: add assertion
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 1.0);
        DfpDec dfp2 = dfp1.nextAfter(dfp1);
        // TODO: add assertion
    }
}