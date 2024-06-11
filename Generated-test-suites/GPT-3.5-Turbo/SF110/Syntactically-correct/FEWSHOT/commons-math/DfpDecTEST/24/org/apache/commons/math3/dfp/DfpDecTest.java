package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {
    
    private DfpDec dfpDec;
    
    @Before
    public void setup() {
        dfpDec = new DfpDec(new DfpField());
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testRound() {
        int result = dfpDec.round(0);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec x = new DfpDec(new DfpField());
        DfpDec result = dfpDec.nextAfter(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance() {
        DfpDec newInstance = dfpDec.newInstance();
        assertNotNull(newInstance);
    }
    
    @Test
    public void testGetDecimalDigits() {
        int result = dfpDec.getDecimalDigits();
        assertEquals(1, result);
    }
    
    @Test
    public void testLessThan() {
        boolean result = dfpDec.lessThan(new DfpDec(new DfpField()));
        assertFalse(result);
    }
    
    @Test
    public void testEquals() {
        boolean result = dfpDec.equals(new DfpDec(new DfpField()));
        assertTrue(result);
    }
    
    @Test
    public void testCopysign() {
        DfpDec result = DfpDec.copysign(dfpDec, dfpDec);
        assertNotNull(result);
    }
    
    @Test
    public void testSubtract() {
        DfpDec result = dfpDec.subtract(dfpDec);
        assertNotNull(result);
    }
    
    @Test
    public void testIntLog10() {
        int result = dfpDec.intLog10();
        assertEquals(0, result);
    }
    
    @Test
    public void testClassify() {
        int result = dfpDec.classify();
        assertEquals(0, result);
    }

}