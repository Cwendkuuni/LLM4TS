package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testConstructor1() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getScale());
        assertEquals(0, dfpDec.getField().getRadixDigits());
        assertEquals(1, dfpDec.getRadix());
    }

    @Test
    public void testConstructor2() {
        DfpDec dfpDec = new DfpDec(10);
        assertEquals(10, dfpDec.getScale());
        assertEquals(0, dfpDec.getField().getRadixDigits());
        assertEquals(1, dfpDec.getRadix());
    }

    @Test
    public void testConstructor3() {
        DfpDec dfpDec = new DfpDec(10, 5);
        assertEquals(10, dfpDec.getScale());
        assertEquals(5, dfpDec.getField().getRadixDigits());
        assertEquals(1, dfpDec.getRadix());
    }

    @Test
    public void testConstructor4() {
        DfpDec dfpDec = new DfpDec(10, 5, 100);
        assertEquals(10, dfpDec.getScale());
        assertEquals(5, dfpDec.getField().getRadixDigits());
        assertEquals(100, dfpDec.getRadix());
    }

    @Test
    public void testConstructor5() {
        DfpDec dfpDec = new DfpDec(10, 5, 100, -1);
        assertEquals(10, dfpDec.getScale());
        assertEquals(5, dfpDec.getField().getRadixDigits());
        assertEquals(100, dfpDec.getRadix());
        assertEquals(-1, dfpDec.getExponent());
    }

    @Test
    public void testConstructor6() {
        DfpDec dfpDec = new DfpDec(10, 5, 100, -1, false);
        assertEquals(10, dfpDec.getScale());
        assertEquals(5, dfpDec.getField().getRadixDigits());
        assertEquals(100, dfpDec.getRadix());
        assertEquals(-1, dfpDec.getExponent());
        assertFalse(dfpDec.isInfinite());
        assertFalse(dfpDec.isNaN());
        assertFalse(dfpDec.isPositiveInfinity());
        assertFalse(dfpDec.isNegativeInfinity());
        assertFalse(dfpDec.isZero());
        assertFalse(dfpDec.isNormal());
        assertFalse(dfpDec.isSubnormal());
        assertFalse(dfpDec.isFinite());
        assertFalse(dfpDec.isEqual(dfpDec));
        assertFalse(dfpDec.isGreaterThanOrEqual(dfpDec));
        assertFalse(dfpDec.isGreaterThan(dfpDec));
        assertFalse(dfpDec.isLessThanOrEqual(dfpDec));
        assertFalse(dfpDec.isLessThan(dfpDec));
        assertNotNull(dfpDec.getField());
        assertNotNull(dfpDec.mant);
    }

    @Test
    public void testConstructor7() {
        DfpDec dfpDec = new DfpDec(10, 5, 100, -1, true);
        assertEquals(10, dfpDec.getScale());
        assertEquals(5, dfpDec.getField().getRadixDigits());
        assertEquals(100, dfpDec.getRadix());
        assertEquals(-1, dfpDec.getExponent());
        assertTrue(dfpDec.isInfinite());
        assertFalse(dfpDec.isNaN());
        assertFalse(dfpDec.isPositiveInfinity());
        assertFalse(dfpDec.isNegativeInfinity());
        assertFalse(dfpDec.isZero());
        assertFalse(dfpDec.isNormal());
        assertFalse(dfpDec.isSubnormal());
        assertFalse(dfpDec.isFinite());
        assertFalse(dfpDec.isEqual(dfpDec));
        assertFalse(dfpDec.isGreaterThanOrEqual(dfpDec));
        assertFalse(dfpDec.isGreaterThan(dfpDec));
        assertFalse(dfpDec.isLessThanOrEqual(dfpDec));
        assertFalse(dfpDec.isLessThan(dfpDec));
        assertNotNull(dfpDec.getField());
        assertNotNull(dfpDec.mant);
    }

    @Test
    public void testAdd() {
        DfpDec dfpDec1 = new DfpDec();
        DfpDec dfpDec2 = new DfpDec();
        DfpDec dfpDec3 = dfpDec1.add(dfpDec2);
        assertNotNull(dfpDec3);
    }

    @Test
    public void testDivide() {
        DfpDec dfpDec1 = new DfpDec();
        DfpDec dfpDec2 = new DfpDec();
        DfpDec dfpDec3 = dfpDec1.divide(dfpDec2);
        assertNotNull(dfpDec3);
    }

    @Test
    public void testMultiply() {
        DfpDec dfpDec1 = new DfpDec();
        DfpDec dfpDec2 = new DfpDec();
        DfpDec dfpDec3 = dfpDec1.multiply(dfpDec2);
        assertNotNull(dfpDec3);
    }

    @Test
    public void testRemainder() {
        DfpDec dfpDec1 = new DfpDec();
        DfpDec dfpDec2 = new DfpDec();
        DfpDec dfpDec3 = dfpDec1.remainder(dfpDec2);
        assertNotNull(dfpDec3);
    }

    @Test
    public void testSubtract() {
        DfpDec dfpDec1 = new DfpDec();
        DfpDec dfpDec2 = new DfpDec();
        DfpDec dfpDec3 = dfpDec1.subtract(dfpDec2);
        assertNotNull(dfpDec3);
    }

    @Test
    public void testToInt() {
        DfpDec dfpDec = new DfpDec();
        int i = dfpDec.toInt();
        assertNotNull(i);
    }

    @Test
    public void testToLong() {
        DfpDec dfpDec = new DfpDec();
        long l = dfpDec.toLong();
        assertNotNull(l);
    }

    @Test
    public void testToString() {
        DfpDec dfpDec = new DfpDec();
        String s = dfpDec.toString();
        assertNotNull(s);
    }

    @Test
    public void testEquals() {
        DfpDec dfpDec1 = new DfpDec();
        DfpDec dfpDec2 = new DfpDec();
        assertTrue(dfpDec1.equals(dfpDec2));
    }

    @Test
    public void testHashCode() {
        DfpDec dfpDec = new DfpDec();
        int hashCode = dfpDec.hashCode();
        assertNotNull(hashCode);
    }
}