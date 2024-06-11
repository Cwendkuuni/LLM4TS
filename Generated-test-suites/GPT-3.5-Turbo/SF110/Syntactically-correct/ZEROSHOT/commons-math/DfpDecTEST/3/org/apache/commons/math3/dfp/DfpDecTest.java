package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpDec dec = new DfpDec(new DfpField());
        assertNotNull(dec);
    }

    @Test
    public void testConstructor2() {
        DfpDec dec = new DfpDec(new DfpField(), (byte) 1);
        assertNotNull(dec);
    }

    @Test
    public void testConstructor3() {
        DfpDec dec = new DfpDec(new DfpField(), 10);
        assertNotNull(dec);
    }

    @Test
    public void testConstructor4() {
        DfpDec dec = new DfpDec(new DfpField(), 100L);
        assertNotNull(dec);
    }

    @Test
    public void testConstructor5() {
        DfpDec dec = new DfpDec(new DfpField(), 10.5);
        assertNotNull(dec);
    }

    @Test
    public void testConstructor6() {
        Dfp d = new Dfp(new DfpField());
        DfpDec dec = new DfpDec(d);
        assertNotNull(dec);
    }

    @Test
    public void testConstructor7() {
        DfpDec dec = new DfpDec(new DfpField(), "10");
        assertNotNull(dec);
    }

    @Test
    public void testConstructor8() {
        DfpDec dec = new DfpDec(new DfpField(), (byte) 1, (byte) 2);
        assertNotNull(dec);
    }

    @Test
    public void testNewInstance1() {
        DfpDec dec = new DfpDec(new DfpField());
        Dfp instance = dec.newInstance();
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstance2() {
        DfpDec dec = new DfpDec(new DfpField(), (byte) 1);
        Dfp instance = dec.newInstance((byte) 1);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
        assertEquals((byte) 1, instance.byteValue());
    }

    @Test
    public void testNewInstance3() {
        DfpDec dec = new DfpDec(new DfpField(), 10);
        Dfp instance = dec.newInstance(10);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
        assertEquals(10, instance.intValue());
    }

    @Test
    public void testNewInstance4() {
        DfpDec dec = new DfpDec(new DfpField(), 100L);
        Dfp instance = dec.newInstance(100L);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
        assertEquals(100L, instance.longValue());
    }

    @Test
    public void testNewInstance5() {
        DfpDec dec = new DfpDec(new DfpField(), 10.5);
        Dfp instance = dec.newInstance(10.5);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
        assertEquals(10.5, instance.doubleValue(), 0.0001);
    }

    @Test
    public void testNewInstance6() {
        Dfp d = new Dfp(new DfpField());
        DfpDec dec = new DfpDec(new DfpField(), d);
        Dfp instance = dec.newInstance(d);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstance7() {
        DfpDec dec = new DfpDec(new DfpField(), "10");
        Dfp instance = dec.newInstance("10");
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
        assertEquals("10", instance.toString());
    }

    @Test
    public void testNewInstance8() {
        DfpDec dec = new DfpDec(new DfpField(), (byte) 1, (byte) 2);
        Dfp instance = dec.newInstance((byte) 1, (byte) 2);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
        assertEquals((byte) 1, instance.byteValue());
        assertEquals((byte) 2, instance.nans);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dec = new DfpDec(new DfpField());
        int decimalDigits = dec.getDecimalDigits();
        assertEquals((dec.getRadixDigits() * 4) - 3, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dec = new DfpDec(new DfpField());
        int round = dec.round(5);
        assertEquals(0, round);
    }

    @Test
    public void testNextAfter() {
        DfpDec dec1 = new DfpDec(new DfpField(), 10);
        DfpDec dec2 = new DfpDec(new DfpField(), 20);
        Dfp dec3 = dec1.nextAfter(dec2);
        assertNotNull(dec3);
        assertTrue(dec3 instanceof DfpDec);
    }
}