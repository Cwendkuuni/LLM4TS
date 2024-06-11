package org.apache.commons.math3.dfp;

import org.junit.Test;

import static org.junit.Assert.*;

public class DfpDecTest {
    @Test
    public void testConstructor() {
        DfpDec dec = new DfpDec(factory);
        assertNotNull(dec);

        DfpDec decByte = new DfpDec(factory, (byte) 1);
        assertNotNull(decByte);

        DfpDec decInt = new DfpDec(factory, 1);
        assertNotNull(decInt);

        DfpDec decLong = new DfpDec(factory, 1L);
        assertNotNull(decLong);

        DfpDec decDouble = new DfpDec(factory, 1.0);
        assertNotNull(decDouble);

        DfpDec decDfp = new DfpDec(d);
        assertNotNull(decDfp);

        DfpDec decString = new DfpDec(factory, "1");
        assertNotNull(decString);

        DfpDec decSign = new DfpDec(factory, (byte) 1, (byte) 2);
        assertNotNull(decSign);
    }

    @Test
    public void testNewInstance() {
        DfpDec decNewInstance = new DfpDec(factory).newInstance();
        assertNotNull(decNewInstance);

        DfpDec decNewInstanceByte = new DfpDec(factory).newInstance((byte) 1);
        assertNotNull(decNewInstanceByte);

        DfpDec decNewInstanceInt = new DfpDec(factory).newInstance(1);
        assertNotNull(decNewInstanceInt);

        DfpDec decNewInstanceLong = new DfpDec(factory).newInstance(1L);
        assertNotNull(decNewInstanceLong);

        DfpDec decNewInstanceDouble = new DfpDec(factory).newInstance(1.0);
        assertNotNull(decNewInstanceDouble);

        DfpDec decNewInstanceDfp = new DfpDec(factory).newInstance(d);
        assertNotNull(decNewInstanceDfp);

        DfpDec decNewInstanceString = new DfpDec(factory).newInstance("1");
        assertNotNull(decNewInstanceString);

        DfpDec decNewInstanceSign = new DfpDec(factory).newInstance((byte) 1, (byte) 2);
        assertNotNull(decNewInstanceSign);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dec = new DfpDec(factory);
        int decimalDigits = dec.getDecimalDigits();
        assertEquals(1, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dec = new DfpDec(factory);
        int round = dec.round(0);
        assertEquals(0, round);
    }

    @Test
    public void testNextAfter() {
        DfpDec decNextAfter = new DfpDec(factory).nextAfter(x);
        assertNotNull(decNextAfter);
    }
}