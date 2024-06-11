
package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegFiveParameterConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegSevenParameterConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "SELL", "NASDAQ", ComboLeg.CLOSE, 1, "LOC");
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(ComboLeg.CLOSE, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("LOC", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentObjectClass() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(comboLeg.equals("ComboLeg"));
    }
    
    @Test
    public void testEqualsWithEqualComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.OPEN);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "LOC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 2, "LOC");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, null);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "LOC");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}
