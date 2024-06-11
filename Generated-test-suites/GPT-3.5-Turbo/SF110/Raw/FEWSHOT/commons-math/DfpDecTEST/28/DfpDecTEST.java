package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(0, dfp.sign);
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(0, dfp.mant[0]);
    }

    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 3);
        assertEquals(1, dfp.sign);
        assertEquals(3, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(1, dfp.mant[0]);
    }

    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 5);
        assertEquals(1, dfp.sign);
        assertEquals(5, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(5, dfp.mant[0]);
    }

    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 1234L);
        assertEquals(1, dfp.sign);
        assertEquals(1234, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(1234, dfp.mant[0]);
    }

    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.23);
        assertEquals(1, dfp.sign);
        assertEquals(2, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(123, dfp.mant[0]);
    }

    @Test
    public void testConstructor6() {
        DfpDec dfp = new DfpDec(new DfpField(), new Dfp(1.23));
        assertEquals(1, dfp.sign);
        assertEquals(2, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(123, dfp.mant[0]);
    }

    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "123");
        assertEquals(1, dfp.sign);
        assertEquals(3, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(123, dfp.mant[0]);
    }

    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 2);
        assertEquals(1, dfp.sign);
        assertEquals(1, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(0, dfp.mant[0]);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance();
        assertTrue(dfp instanceof DfpDec);
    }

    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 3);
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(3, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(1, dfp.mant[0]);
    }

    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(5);
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(5, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(5, dfp.mant[0]);
    }

    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1234L);
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(1234, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(1234, dfp.mant[0]);
    }

    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1.23);
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(2, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(123, dfp.mant[0]);
    }

    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(new Dfp(1.23));
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(2, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(123, dfp.mant[0]);
    }

    @Test
    public void testNewInstance6() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance("123");
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(3, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(123, dfp.mant[0]);
    }

    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 2);
        assertTrue(dfp instanceof DfpDec);
        assertEquals(1, dfp.sign);
        assertEquals(1, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(0, dfp.mant[0]);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(1, dfp.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(0, dfp.round(0));
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(new DfpField());
        DfpDec dfp2 = dfp1.nextAfter(new DfpDec(new DfpField(), 10));
        assertTrue(dfp2 instanceof DfpDec);
        assertEquals(1, dfp2.sign);
        assertEquals(1, dfp2.exp);
        assertEquals(1, dfp2.mant.length);
        assertEquals(0, dfp2.mant[0]);
    }
}