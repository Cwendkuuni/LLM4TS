package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {
    private DfpDec dfp;

    @Before
    public void setUp() {
        dfp = new DfpDec(3);
    }
    
    @Test
    public void testDfpDecConstructorWithField() {
        assertEquals(3, dfp.getField().getRadixDigits());
        assertEquals(0, dfp.sign);
        assertEquals(0, dfp.exp);
        assertEquals(0, dfp.mant[0]);
    }
    
    @Test
    public void testDfpDecConstructorWithFieldAndByte() {
        DfpDec result = new DfpDec(dfp.getField(), (byte) -3);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(-1, result.sign);
        assertEquals(0, result.exp);
        assertEquals(3, result.mant[0]);
    }
    
    @Test
    public void testDfpDecConstructorWithFieldAndInt() {
        DfpDec result = new DfpDec(dfp.getField(), -5);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(-1, result.sign);
        assertEquals(0, result.exp);
        assertEquals(5, result.mant[0]);
    }
    
    @Test
    public void testDfpDecConstructorWithFieldAndLong() {
        DfpDec result = new DfpDec(dfp.getField(), 10L);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(10, result.mant[0]);
    }
    
    @Test
    public void testDfpDecConstructorWithFieldAndDouble() {
        DfpDec result = new DfpDec(dfp.getField(), 2.5);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(25, result.mant[0]);
    }
    
    @Test
    public void testDfpDecConstructorWithDfp() {
        DfpDec d = new DfpDec(dfp);
        assertEquals(3, d.getField().getRadixDigits());
        assertEquals(0, d.sign);
        assertEquals(0, d.exp);
        assertEquals(0, d.mant[0]);
    }
    
    @Test
    public void testDfpDecConstructorWithFieldAndString() {
        DfpDec result = new DfpDec(dfp.getField(), "12345");
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(123, result.mant[0]);
        assertEquals(450, result.mant[1]);
    }
    
    @Test
    public void testDfpDecConstructorWithFieldAndByteByte() {
        DfpDec result = new DfpDec(dfp.getField(), (byte) -3, (byte) 3);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(-1, result.sign);
        assertEquals(3, result.exp);
        assertEquals(0, result.mant[0]);
    }
    
    @Test
    public void testNewInstance() {
        Dfp result = dfp.newInstance();
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(0, result.mant[0]);
    }
    
    @Test
    public void testNewInstanceWithByte() {
        Dfp result = dfp.newInstance((byte) -3);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(-1, result.sign);
        assertEquals(0, result.exp);
        assertEquals(3, result.mant[0]);
    }
    
    @Test
    public void testNewInstanceWithInt() {
        Dfp result = dfp.newInstance(-5);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(-1, result.sign);
        assertEquals(0, result.exp);
        assertEquals(5, result.mant[0]);
    }
    
    @Test
    public void testNewInstanceWithLong() {
        Dfp result = dfp.newInstance(10L);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(10, result.mant[0]);
    }
    
    @Test
    public void testNewInstanceWithDouble() {
        Dfp result = dfp.newInstance(2.5);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(25, result.mant[0]);
    }
    
    @Test
    public void testNewInstanceWithDfp() {
        Dfp result = dfp.newInstance(dfp);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(0, result.mant[0]);
    }
    
    @Test
    public void testNewInstanceWithString() {
        Dfp result = dfp.newInstance("12345");
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(123, result.mant[0]);
        assertEquals(450, result.mant[1]);
    }
    
    @Test
    public void testNewInstanceWithByteByte() {
        Dfp result = dfp.newInstance((byte) -3, (byte) 3);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(-1, result.sign);
        assertEquals(3, result.exp);
        assertEquals(0, result.mant[0]);
    }
    
    @Test
    public void testGetDecimalDigits() {
        int result = dfp.getDecimalDigits();
        assertEquals(9, result);
    }
    
    @Test
    public void testRoundWithZeroInput() {
        int result = dfp.round(0);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfterWithEqualValues() {
        Dfp result = dfp.nextAfter(dfp);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(0, result.mant[0]);
    }
    
    @Test
    public void testNextAfterWithLessThanValues() {
        Dfp x = dfp.newInstance(-2);
        Dfp result = dfp.nextAfter(x);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(0, result.mant[0]);
    }
    
    @Test
    public void testNextAfterWithGreaterThanValues() {
        Dfp x = dfp.newInstance(4);
        Dfp result = dfp.nextAfter(x);
        assertEquals(3, result.getField().getRadixDigits());
        assertEquals(0, result.sign);
        assertEquals(0, result.exp);
        assertEquals(0, result.mant[0]);
    }
}