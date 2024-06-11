package org.apache.commons.math3.dfp;

import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(DfpDec.getField(), 123);
        int result = dfpDec.getDecimalDigits();
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(DfpDec.getField(), 0.999);
        int result = dfpDec.round(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(DfpDec.getField(), 1);
        DfpDec dfpDec2 = new DfpDec(DfpDec.getField(), -1);

        DfpDec result1 = dfpDec1.nextAfter(dfpDec2);
        DfpDec result2 = dfpDec2.nextAfter(dfpDec1);

        Assert.assertEquals(1, result1.compare(dfpDec2));
        Assert.assertEquals(-1, result2.compare(dfpDec1));
    }
}