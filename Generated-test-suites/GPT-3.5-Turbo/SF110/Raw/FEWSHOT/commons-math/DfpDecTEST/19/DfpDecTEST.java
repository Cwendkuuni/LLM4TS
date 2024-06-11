package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {
    private DfpDec dfpDec;
    
    @Test
    public void testConstructor() {
        DfpField dfpField = new DfpField();
        dfpDec = new DfpDec(dfpField);
        assertNull(dfpDec.toString());
        
        dfpDec = new DfpDec(dfpField, (byte) 5);
        assertEquals(5, dfpDec.byteValue());
        
        dfpDec = new DfpDec(dfpField, 10);
        assertEquals(10, dfpDec.intValue());
        
        dfpDec = new DfpDec(dfpField, 100L);
        assertEquals(100L, dfpDec.longValue());
        
        dfpDec = new DfpDec(dfpField, 1.5);
        assertTrue(1.5 == dfpDec.doubleValue());
    }
    
    @Test
    public void testCopyConstructor() {
        DfpField dfpField = new DfpField();
        Dfp d = new Dfp(dfpField);
        dfpDec = new DfpDec(d);
        assertTrue(d.equals(dfpDec));
        
        dfpDec = new DfpDec(dfpField, "5");
        Dfp newDec = new Dfp(dfpField);
        assertTrue(dfpDec.equals(dfpDec.newInstance(newDec)));
        
        dfpDec = new DfpDec(dfpField, (byte) 5, (byte) 0);
        assertTrue(dfpDec.equals(dfpDec.newInstance((byte) 5, (byte) 0)));
    }
    
    @Test
    public void testNewInstance() {
        DfpField dfpField = new DfpField();
        dfpDec = new DfpDec(dfpField);
        assertTrue(dfpDec.equals(dfpDec.newInstance()));
        
        dfpDec = new DfpDec(dfpField, "5");
        assertTrue(dfpDec.equals(dfpDec.newInstance((byte) 5)));
        
        dfpDec = new DfpDec(dfpField, (byte) 5, (byte) 0);
        assertTrue(dfpDec.equals(dfpDec.newInstance(5)));
        
        dfpDec = new DfpDec(dfpField, (byte) 5, (byte) 0);
        assertTrue(dfpDec.equals(dfpDec.newInstance(100L)));
        
        dfpDec = new DfpDec(dfpField, (byte) 5, (byte) 0);
        assertTrue(dfpDec.equals(dfpDec.newInstance(1.5)));
        
        Dfp d = new Dfp(dfpField);
        assertTrue(dfpDec.equals(dfpDec.newInstance(d)));
        
        dfpDec = new DfpDec(dfpField, (byte) 5, (byte) 0);
        assertTrue(dfpDec.equals(dfpDec.newInstance("5")));
        
        dfpDec = new DfpDec(dfpField, (byte) 5, (byte) 0);
        assertTrue(dfpDec.equals(dfpDec.newInstance((byte) 5, (byte) 0)));
    }
    
    @Test
    public void testGetDecimalDigits() {
        dfpDec = new DfpDec(new DfpField());
        assertEquals(11, dfpDec.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        dfpDec = new DfpDec(new DfpField());
        assertEquals(0, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[5];
        dfpDec.mant[4] = 1000;
        assertEquals(0, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[5];
        dfpDec.mant[4] = 10001;
        assertEquals(16, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[5];
        dfpDec.mant[4] = 1000;
        assertEquals(0, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[4];
        dfpDec.mant[3] = 1000;
        assertEquals(0, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[4];
        dfpDec.mant[3] = 10001;
        assertEquals(16, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[5];
        dfpDec.mant[4] = 1000;
        dfpDec.setIEEEFlagsBits(8);
        assertEquals(8, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[5];
        dfpDec.mant[4] = 1000;
        dfpDec.setIEEEFlagsBits(4);
        assertEquals(4, dfpDec.round(0));
        
        dfpDec = new DfpDec(new DfpField(), 1.5);
        dfpDec.mant = new int[5];
        dfpDec.mant[4] = 1000;
        dfpDec.setIEEEFlagsBits(16);
        assertEquals(16, dfpDec.round(0));
    }
    
    @Test
    public void testNextAfter() {
        DfpField dfpField = new DfpField();
        dfpDec = new DfpDec(dfpField);
        Dfp x = new Dfp(dfpField);
        assertTrue(dfpDec.equals(dfpDec.nextAfter(x)));
        
        dfpDec = new DfpDec(dfpField, "5");
        assertEquals(1, dfpDec.nextAfter(dfpDec).classify());
        
        dfpDec = new DfpDec(dfpField, "0");
        assertEquals(10, dfpDec.nextAfter(dfpDec).classify());
        
        dfpDec = new DfpDec(dfpField, "0");
        assertEquals(0, dfpDec.nextAfter(dfpDec).classify());
        
        dfpDec = new DfpDec(dfpField, "-5");
        assertEquals(1, dfpDec.nextAfter(dfpDec).classify());
    }
}