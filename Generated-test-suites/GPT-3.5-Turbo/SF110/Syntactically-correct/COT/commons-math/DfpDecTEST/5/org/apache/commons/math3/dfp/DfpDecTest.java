
package org.apache.commons.math3.dfp;

import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor6() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfp = new DfpDec(d);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "1");
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 2);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance();
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1L);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1.0);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance6() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(d);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance("1");
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 2);
        Assert.assertNotNull(dfp);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int decimalDigits = dfp.getDecimalDigits();
        Assert.assertEquals(16, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int rounded = dfp.round(1);
        Assert.assertEquals(0, rounded);
    }

    @Test
    public void testNextAfter() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec nextAfter = dfp.nextAfter(d);
        Assert.assertNotNull(nextAfter);
    }

}

