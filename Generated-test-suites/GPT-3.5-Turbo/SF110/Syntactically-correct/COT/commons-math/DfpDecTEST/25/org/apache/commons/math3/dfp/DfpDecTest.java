
package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDec() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecByte() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecInt() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecLong() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1L);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1.0);
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecDfp() {
        DfpDec dfpDec1 = new DfpDec(new DfpField());
        DfpDec dfpDec2 = new DfpDec(dfpDec1);
        assertNotNull(dfpDec2);
    }

    @Test
    public void testDfpDecString() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "1.0");
        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecByteByte() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance();
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceByte() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance((byte) 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceInt() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceLong() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1L);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance(1.0);
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpDec dfpDec1 = new DfpDec(new DfpField());
        DfpDec dfpDec2 = new DfpDec(new DfpField()).newInstance(dfpDec1);
        assertNotNull(dfpDec2);
    }

    @Test
    public void testNewInstanceString() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance("1.0");
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpDec dfpDec = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 1);
        assertNotNull(dfpDec);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(23, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int roundValue = dfpDec.round(0);
        assertEquals(0, roundValue);
    }

    @Test
    public void testRoundWithNonZeroMsb() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1.0);
        int roundValue = dfpDec.round(0);
        assertEquals(0, roundValue);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(new DfpField(), 1.0);
        DfpDec nextAfter = dfpDec1.nextAfter(dfpDec1);
        assertEquals(dfpDec1, nextAfter);
    }
    
    @Test
    public void testNextAfterWithLessThan() {
        DfpDec dfpDec1 = new DfpDec(new DfpField(), 1.0);
        DfpDec dfpDec2 = new DfpDec(new DfpField(), 2.0);
        DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
        assertTrue(nextAfter.greaterThan(dfpDec1));
        assertTrue(nextAfter.lessThan(dfpDec2));
    }
    
    @Test
    public void testNextAfterWithGreaterThan() {
        DfpDec dfpDec1 = new DfpDec(new DfpField(), 2.0);
        DfpDec dfpDec2 = new DfpDec(new DfpField(), 1.0);
        DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
        assertTrue(nextAfter.lessThan(dfpDec1));
        assertTrue(nextAfter.greaterThan(dfpDec2));
    }

}
