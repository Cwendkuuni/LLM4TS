
package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testConstructor1() {
        // Test DfpDec(DfpField)
        DfpDec dfp = new DfpDec(new DfpField());
        
        assertNull(dfp.toString());
        assertEquals(0, dfp.getDouble(), 0);
        assertEquals(0, dfp.intValue());
    }
    
    @Test
    public void testConstructor2() {
        // Test DfpDec(DfpField, byte)
        DfpField field = new DfpField();
        field.setRadixDigits(10);
        DfpDec dfp = new DfpDec(field, (byte) 1);
        
        assertNull(dfp.toString());
        assertEquals(1, dfp.getDouble(), 0);
    }
    
    @Test
    public void testConstructor3() {
        // Test DfpDec(DfpField, int)
        DfpField field = new DfpField();
        field.setRadixDigits(10);
        DfpDec dfp = new DfpDec(field, 123);
        
        assertNull(dfp.toString());
        assertEquals(123, dfp.getDouble(), 0);
    }
    
    @Test
    public void testConstructor4() {
        // Test DfpDec(DfpField, long)
        DfpField field = new DfpField();
        field.setRadixDigits(10);
        DfpDec dfp = new DfpDec(field, 123L);
        
        assertNull(dfp.toString());
        assertEquals(123L, dfp.getDouble(), 0);
    }
    
    @Test
    public void testConstructor5() {
        // Test DfpDec(DfpField, double)
        DfpField field = new DfpField();
        field.setRadixDigits(10);
        DfpDec dfp = new DfpDec(field, 3.14);
        
        assertNull(dfp.toString());
        assertEquals(3.14, dfp.getDouble(), 0);
    }
    
    @Test
    public void testConstructor6() {
        // Test DfpDec(Dfp)
        Dfp dfp = new Dfp(new DfpField(), 3.14);
        DfpDec decimal = new DfpDec(dfp);
        
        assertEquals(dfp.toString(), decimal.toString());
        assertEquals(dfp.getDouble(), decimal.getDouble(), 0);
    }
    
    @Test
    public void testConstructor7() {
        // Test DfpDec(DfpField, String)
        DfpField field = new DfpField();
        field.setRadixDigits(10);
        DfpDec dfp = new DfpDec(field, "3.14");
        
        assertEquals("3.14", dfp.toString());
        assertEquals(3.14, dfp.getDouble(), 0);
    }
    
    @Test
    public void testConstructor8() {
        // Test DfpDec(DfpField, byte, byte)
        DfpField field = new DfpField();
        field.setRadixDigits(10);
        DfpDec dfp = new DfpDec(field, (byte) 1, (byte) 0);
        
        assertNull(dfp.toString());
        assertEquals(1, dfp.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance1() {
        // Test newInstance()
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance();
        
        assertNull(newInstance.toString());
        assertEquals(0, newInstance.getDouble(), 0);
        assertEquals(0, newInstance.intValue());
    }
    
    @Test
    public void testNewInstance2() {
        // Test newInstance(byte)
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance((byte) 1);
        
        assertNull(newInstance.toString());
        assertEquals(1, newInstance.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance3() {
        // Test newInstance(int)
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance(123);
        
        assertNull(newInstance.toString());
        assertEquals(123, newInstance.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance4() {
        // Test newInstance(long)
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance(123L);
        
        assertNull(newInstance.toString());
        assertEquals(123L, newInstance.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance5() {
        // Test newInstance(double)
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance(3.14);
        
        assertNull(newInstance.toString());
        assertEquals(3.14, newInstance.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance6() {
        // Test newInstance(Dfp)
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp newInstance = new Dfp(new DfpField(), 3.14);
        DfpDec decimal = dfp.newInstance(newInstance);
        
        assertEquals(newInstance.toString(), decimal.toString());
        assertEquals(newInstance.getDouble(), decimal.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance7() {
        // Test newInstance(String)
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance("3.14");
        
        assertEquals("3.14", newInstance.toString());
        assertEquals(3.14, newInstance.getDouble(), 0);
    }
    
    @Test
    public void testNewInstance8() {
        // Test newInstance(byte, byte)
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance((byte) 1, (byte) 0);
        
        assertNull(newInstance.toString());
        assertEquals(1, newInstance.getDouble(), 0);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        
        assertEquals(37, dfp.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        
        assertEquals(0, dfp.round(0));
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec x = new DfpDec(new DfpField(), 3.14);
        
        DfpDec nextAfter = dfp.nextAfter(x);
        
        assertEquals("3.14", nextAfter.toString());
        assertEquals(3.14, nextAfter.getDouble(), 0);
    }
}
