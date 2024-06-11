package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {

    private DfpDec dfpDec;

    @Before
    public void setUp() {
        DfpField dfpField = new DfpField();
        dfpDec = new DfpDec(dfpField);
    }

    @Test
    public void testConstructor() {
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithByte() {
        DfpDec result = dfpDec.newInstance((byte) 1);
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithInt() {
        DfpDec result = dfpDec.newInstance(10);
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithLong() {
        DfpDec result = dfpDec.newInstance(10000L);
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithDouble() {
        DfpDec result = dfpDec.newInstance(10.5);
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithDfp() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec result = dfpDec.newInstance(dfp);
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithString() {
        DfpDec result = dfpDec.newInstance("10.5");
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithByteAndSign() {
        DfpDec result = dfpDec.newInstance((byte) 1, (byte) 1);
        assertNotNull(result);
    }

    @Test
    public void testGetDecimalDigits() {
        int result = dfpDec.getDecimalDigits();
        assertEquals(29, result);
    }

    @Test
    public void testRound() {
        int result = dfpDec.round(5);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec x = dfpDec.newInstance(10.5);
        DfpDec result = dfpDec.nextAfter(x);
        assertNotNull(result);
    }
}