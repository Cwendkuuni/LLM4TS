package org.apache.commons.math3.dfp;

import org.junit.*;
import static org.junit.Assert.*;

public class DfpDecTest {
    
    private DfpDec dfpDec;
    
    @Before
    public void setUp() {
        dfpDec = new DfpDec(new DfpField());
    }
    
    @Test
    public void testNewInstance() {
        Dfp result = dfpDec.newInstance();
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceByte() {
        Dfp result = dfpDec.newInstance((byte) 1);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceInt() {
        Dfp result = dfpDec.newInstance(1);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceLong() {
        Dfp result = dfpDec.newInstance(1L);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceDouble() {
        Dfp result = dfpDec.newInstance(1.0);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceDfp() {
        Dfp d = new Dfp();
        Dfp result = dfpDec.newInstance(d);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceString() {
        String s = "1.0";
        Dfp result = dfpDec.newInstance(s);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceByteByte() {
        Dfp result = dfpDec.newInstance((byte) 1, (byte) 1);
        assertNotNull(result);
    }
    
    @Test
    public void testGetDecimalDigits() {
        int result = dfpDec.getDecimalDigits();
        assertNotNull(result);
    }
    
    @Test
    public void testRound() {
        int result = dfpDec.round(1);
        assertNotNull(result);
    }
    
    @Test
    public void testNextAfter() {
        Dfp d = new Dfp();
        Dfp result = dfpDec.nextAfter(d);
        assertNotNull(result);
    }
}