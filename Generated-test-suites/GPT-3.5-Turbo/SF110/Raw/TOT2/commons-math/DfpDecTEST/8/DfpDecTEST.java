package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(10);
        Dfp newInstance = dfpDec.newInstance();
        assertEquals(dfpDec, newInstance);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(10);
        assertEquals(37, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(10.5);
        int result = dfpDec.round(0);
        assertEquals(16, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(10);
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(5));
        assertNotEquals(dfpDec, nextAfter);
    }
}