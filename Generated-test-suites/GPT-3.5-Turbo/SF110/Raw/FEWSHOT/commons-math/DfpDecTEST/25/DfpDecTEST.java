package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {
    
    private DfpDec dfpDec;
    
    @Before
    public void setUp() {
        DfpField field = new DfpField(16);
        this.dfpDec = new DfpDec(field);
    }
    
    // Test cases for DfpDec constructor
    
    @Test
    public void testConstructorWithField() {
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructorWithFieldAndByte() {
        DfpDec dfp = new DfpDec(dfpDec.getField(), (byte) 2);
        assertEquals(2, dfp.intValue());
    }
    
    @Test
    public void testConstructorWithFieldAndInt() {
        DfpDec dfp = new DfpDec(dfpDec.getField(), 3);
        assertEquals(3, dfp.intValue());
    }
    
    @Test
    public void testConstructorWithFieldAndLong() {
        DfpDec dfp = new DfpDec(dfpDec.getField(), 4);
        assertEquals(4, dfp.longValue());
    }
    
    @Test
    public void testConstructorWithFieldAndDouble() {
        DfpDec dfp = new DfpDec(dfpDec.getField(), 5.0);
        assertEquals(5.0, dfp.doubleValue(), 0.001);
    }
    
    @Test
    public void testConstructorWithDfp() {
        Dfp d = new Dfp(5.0);
        DfpDec dfp = new DfpDec(d);
        assertEquals(5.0, dfp.doubleValue(), 0.001);
    }
    
    @Test
    public void testConstructorWithString() {
        DfpDec dfp = new DfpDec(dfpDec.getField(), "5");
        assertEquals(5, dfp.intValue());
    }
    
    @Test
    public void testConstructorWithSignAndNans() {
        DfpDec dfp = new DfpDec(dfpDec.getField(), (byte) 1, (byte) 2);
        assertEquals(1, dfp.getSign());
        assertEquals(2, dfp.getNans());
    }
    
    // Test cases for newInstance method
    
    @Test
    public void testNewInstance() {
        Dfp result = dfpDec.newInstance();
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceWithByte() {
        Dfp result = dfpDec.newInstance((byte) 2);
        assertEquals(2, result.intValue());
    }
    
    @Test
    public void testNewInstanceWithInt() {
        Dfp result = dfpDec.newInstance(3);
        assertEquals(3, result.intValue());
    }
    
    @Test
    public void testNewInstanceWithLong() {
        Dfp result = dfpDec.newInstance(4);
        assertEquals(4, result.longValue());
    }
    
    @Test
    public void testNewInstanceWithDouble() {
        Dfp result = dfpDec.newInstance(5.0);
        assertEquals(5.0, result.doubleValue(), 0.001);
    }
    
    @Test
    public void testNewInstanceWithDfp() {
        Dfp d = new Dfp(5.0);
        Dfp result = dfpDec.newInstance(d);
        assertEquals(5.0, result.doubleValue(), 0.001);
    }
    
    @Test
    public void testNewInstanceWithString() {
        Dfp result = dfpDec.newInstance("5");
        assertEquals(5, result.intValue());
    }
    
    @Test
    public void testNewInstanceWithSignAndNans() {
        Dfp result = dfpDec.newInstance((byte) 1, (byte) 2);
        assertEquals(1, result.getSign());
        assertEquals(2, result.getNans());
    }
    
    // Test cases for getDecimalDigits method
    
    @Test
    public void testGetDecimalDigits() {
        int result = dfpDec.getDecimalDigits();
        assertEquals(13, result);
    }
    
    // Test cases for round method
    
    @Test
    public void testRound() {
        int result = dfpDec.round(2);
        assertEquals(2, result);
    }
    
    // Test cases for nextAfter method
    
    @Test
    public void testNextAfter() {
        Dfp d = new Dfp(5.0);
        Dfp result = dfpDec.nextAfter(d);
        assertEquals(5.0, result.doubleValue(), 0.001);
    }

}