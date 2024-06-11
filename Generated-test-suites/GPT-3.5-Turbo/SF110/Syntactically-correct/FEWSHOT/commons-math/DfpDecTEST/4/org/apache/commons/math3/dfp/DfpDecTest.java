package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {
    
    private DfpFactory factory;
    
    @Before
    public void setup() {
        factory = new DfpFactory();
    }

    @Test
    public void testConstructorWithFactory() {
        DfpDec dfpDec = new DfpDec(factory);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndByte() {
        DfpDec dfpDec = new DfpDec(factory, (byte) 2);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndInt() {
        DfpDec dfpDec = new DfpDec(factory, 3);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndLong() {
        DfpDec dfpDec = new DfpDec(factory, 4L);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndDouble() {
        DfpDec dfpDec = new DfpDec(factory, 5.0);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithDfp() {
        Dfp dfp = new Dfp(factory);
        DfpDec dfpDec = new DfpDec(dfp);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndString() {
        DfpDec dfpDec = new DfpDec(factory, "6");
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndByteAndByte() {
        DfpDec dfpDec = new DfpDec(factory, (byte) 7, (byte) 8);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance();
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance((byte) 2);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance(3);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance(4L);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance(5.0);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp1 = new Dfp(factory);
        Dfp dfp2 = dfpDec.newInstance(dfp1);
        assertNotNull(dfp2);
    }

    @Test
    public void testNewInstanceWithString() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance("6");
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstanceWithByteAndByte() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp dfp = dfpDec.newInstance((byte) 7, (byte) 8);
        assertNotNull(dfp);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(factory);
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(11, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(factory);
        int result = dfpDec.round(0);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(factory);
        Dfp result = dfpDec.nextAfter(dfpDec);
        assertNotNull(result);
    }

}