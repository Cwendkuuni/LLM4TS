package org.apache.commons.math3.dfp;
import static org.junit.Assert.*;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp);
    }
    
    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(null, (byte) 1);
        assertNull(dfp);
    }
    
    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(null, 1);
        assertNull(dfp);
    }
    
    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(null, 1L);
        assertNull(dfp);
    }

    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(null, 1.0);
        assertNull(dfp);
    }
    
    @Test
    public void testConstructor6() {
        DfpDec dfp1 = new DfpDec(null, new Dfp());
        DfpDec dfp2 = dfp1.newInstance();
        assertNotNull(dfp1);
        assertNotNull(dfp2);
        assertEquals(dfp1, dfp2);
    }
    
    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(null, "1.0");
        assertNull(dfp);
    }
    
    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(null, (byte) 1, (byte) 1);
        assertNull(dfp);
    }

    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance());
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance((byte) 1));
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance(1));
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance(1L));
    }

    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance(1.0));
    }
    
    @Test
    public void testNewInstance6() {
        DfpDec dfp1 = new DfpDec(null, new Dfp());
        DfpDec dfp2 = dfp1.newInstance(dfp1);
        assertNotNull(dfp1);
        assertNotNull(dfp2);
        assertEquals(dfp1, dfp2);
    }
    
    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance("1.0"));
    }
    
    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(null);
        assertNull(dfp.newInstance((byte) 1, (byte) 1));
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(null);
        assertEquals(0, dfp.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(null);
        assertEquals(0, dfp.round(0));
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(null, new Dfp());
        DfpDec dfp2 = dfp1.nextAfter(dfp1);
        assertNotNull(dfp2);
    }
}