package org.apache.commons.math3.dfp;

import org.junit.Test;

import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDec() {
        DfpDec dfpDec = new DfpDec(4);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecFactoryByte() {
        DfpDec dfpDec = new DfpDec(null, (byte) 4);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecFactoryInt() {
        DfpDec dfpDec = new DfpDec(null, 4);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecFactoryLong() {
        DfpDec dfpDec = new DfpDec(null, 4L);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecFactoryDouble() {
        DfpDec dfpDec = new DfpDec(null, 4.0);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecDfp() {
        DfpDec arg = new DfpDec(4);
        DfpDec dfpDec = new DfpDec(arg);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecFactoryString() {
        DfpDec dfpDec = new DfpDec(null, "4");
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecFactoryByteByte() {
        DfpDec dfpDec = new DfpDec(null, (byte) 4, (byte) 4);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance();
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceByte() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance((byte) 4);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceInt() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance(4);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceLong() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance(4L);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceDouble() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance(4.0);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp arg = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance(arg);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceString() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance("4");
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp dfp = dfpDec.newInstance((byte) 4, (byte) 4);
        assertNotNull(dfp);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(4);
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(25, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(4);
        int round = dfpDec.round(4);
        assertEquals(0, round);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(4);
        Dfp arg = new DfpDec(4);
        Dfp dfp = dfpDec.nextAfter(arg);
        assertNotNull(dfp);
    }
}