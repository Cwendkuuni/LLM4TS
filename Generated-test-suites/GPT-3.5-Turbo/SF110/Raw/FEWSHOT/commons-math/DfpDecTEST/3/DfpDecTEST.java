
package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.junit.Test;

public class DfpDecTest {
    
    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(0, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 5);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(1, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 10);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(0, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 1024L);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(0, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.14);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(0, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testConstructor6() {
        DfpDec d = new DfpDec(new DfpField(), new Dfp());
        assertNotSame(new DfpField(), d.getField());
        assertEquals(0, d.getSign());
        assertEquals(0, d.getExponent());
        assertArrayEquals(new int[43], d.getMantissa());
    }
    
    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "12345");
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(0, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 2);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(1, dfp.getSign());
        assertEquals(0, dfp.getExponent());
        assertArrayEquals(new int[43], dfp.getMantissa());
    }
    
    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance();
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField()), dfp);
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 5);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField(), (byte) 5), dfp);
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(10);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField(), 10), dfp);
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1024L);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField(), 1024L), dfp);
    }
    
    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(3.14);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField(), 3.14), dfp);
    }
    
    @Test
    public void testNewInstance6() {
        DfpDec d = new DfpDec(new DfpField(), new Dfp()).newInstance(new Dfp());
        assertNotSame(new DfpField(), d.getField());
        assertNotEquals(new DfpDec(new DfpField(), new Dfp()), d);
    }
    
    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance("12345");
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField(), "12345"), dfp);
    }
    
    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 2);
        assertEquals(new DfpField(), dfp.getField());
        assertEquals(new DfpDec(new DfpField(), (byte) 1, (byte) 2), dfp);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertEquals(171, dfp.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int in = 0;
        int result = dfp.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec x = new DfpDec(new DfpField());
        DfpDec result = dfp.nextAfter(x);
        assertEquals(new DfpDec(new DfpField(), new Dfp()), result);
    }
}
