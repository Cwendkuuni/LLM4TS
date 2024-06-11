package org.apache.commons.math3.dfp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    private DfpDec dfpDec;

    @Before
    public void setUp() {
        DfpField field = new DfpField(0);
        dfpDec = new DfpDec(field);
    }

    @Test
    public void testConstructorWithDfpField() {
        assertEquals(dfpDec.getClass(), dfpDec.newInstance().getClass());
    }

    @Test
    public void testConstructorWithByte() {
        byte x = 1;
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(x).getClass());
    }

    @Test
    public void testConstructorWithInt() {
        int x = 1;
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(x).getClass());
    }

    @Test
    public void testConstructorWithLong() {
        long x = 1L;
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(x).getClass());
    }

    @Test
    public void testConstructorWithDouble() {
        double x = 1.0;
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(x).getClass());
    }

    @Test
    public void testConstructorWithDfp() {
        Dfp d = new Dfp(0);
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(d).getClass());
    }

    @Test
    public void testConstructorWithString() {
        String s = "1";
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(s).getClass());
    }

    @Test
    public void testConstructorWithSignAndNans() {
        byte sign = 1;
        byte nans = 1;
        assertEquals(dfpDec.getClass(), dfpDec.newInstance(sign, nans).getClass());
    }

    @Test
    public void testGetDecimalDigits() {
        int expected = -1165;
        assertEquals(expected, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        int in = 0;
        int expected = 0;
        assertEquals(expected, dfpDec.round(in));
    }

    @Test
    public void testNextAfter() {
        Dfp x = new Dfp(0);
        assertEquals(dfpDec.getClass(), dfpDec.nextAfter(x).getClass());
    }

}