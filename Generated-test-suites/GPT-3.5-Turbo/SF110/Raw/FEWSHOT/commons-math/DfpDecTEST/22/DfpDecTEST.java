
package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(0, dfp.mant[0]);
    }

    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 3);
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(3, dfp.mant[0]);
    }

    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 5);
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(5, dfp.mant[0]);
    }

    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 10L);
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(10, dfp.mant[0]);
    }

    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.5);
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(3, dfp.mant[0]);
    }

    @Test
    public void testConstructor6() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 3.5);
        DfpDec dfp2 = new DfpDec(dfp1);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "10");
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(10, dfp.mant[0]);
    }

    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) -1, (byte) 2);
        assertEquals(0, dfp.exp);
        assertEquals(1, dfp.mant.length);
        assertEquals(Integer.MIN_VALUE, dfp.mant[0]);
    }

    @Test
    public void testNewInstance1() {
        DfpDec dfp1 = new DfpDec(new DfpField());
        DfpDec dfp2 = dfp1.newInstance();
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance2() {
        DfpDec dfp1 = new DfpDec(new DfpField(), (byte) 3);
        DfpDec dfp2 = dfp1.newInstance((byte) 2);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance3() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 5);
        DfpDec dfp2 = dfp1.newInstance(7);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance4() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 10L);
        DfpDec dfp2 = dfp1.newInstance(15L);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance5() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 3.5);
        DfpDec dfp2 = dfp1.newInstance(4.5);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance6() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 3.5);
        DfpDec dfp2 = dfp1.newInstance(dfp1);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance7() {
        DfpDec dfp1 = new DfpDec(new DfpField(), "10");
        DfpDec dfp2 = dfp1.newInstance("15");
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testNewInstance8() {
        DfpDec dfp1 = new DfpDec(new DfpField(), (byte) -1, (byte) 2);
        DfpDec dfp2 = dfp1.newInstance((byte) 1, (byte) 3);
        assertTrue(dfp1.equals(dfp2));
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.5);
        assertEquals(13, dfp.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.5);
        assertEquals(0, dfp.round(0));
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 3.5);
        DfpDec dfp2 = dfp1.nextAfter(new DfpDec(new DfpField(), 4.5));
        assertTrue(dfp1.equals(dfp2));
    }
    
}
