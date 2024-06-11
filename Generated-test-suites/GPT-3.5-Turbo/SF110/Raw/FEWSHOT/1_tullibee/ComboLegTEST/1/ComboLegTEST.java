package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testComboLegConstructor() {
        ComboLeg leg1 = new ComboLeg();
        assertEquals(0, leg1.m_conId);
        assertEquals(0, leg1.m_ratio);
        assertEquals(null, leg1.m_action);
        assertEquals(null, leg1.m_exchange);
        assertEquals(0, leg1.m_openClose);
        assertEquals(0, leg1.m_shortSaleSlot);
        assertEquals(null, leg1.m_designatedLocation);
        
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, leg2.m_conId);
        assertEquals(2, leg2.m_ratio);
        assertEquals("BUY", leg2.m_action);
        assertEquals("NYSE", leg2.m_exchange);
        assertEquals(1, leg2.m_openClose);
        assertEquals(0, leg2.m_shortSaleSlot);
        assertEquals(null, leg2.m_designatedLocation);
        
        ComboLeg leg3 = new ComboLeg(3, 4, "SELL", "LSE", 2, 3, "LOC");
        assertEquals(3, leg3.m_conId);
        assertEquals(4, leg3.m_ratio);
        assertEquals("SELL", leg3.m_action);
        assertEquals("LSE", leg3.m_exchange);
        assertEquals(2, leg3.m_openClose);
        assertEquals(3, leg3.m_shortSaleSlot);
        assertEquals("LOC", leg3.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg();
        
        assertEquals(leg1, leg2);
        assertFalse(leg1.equals(leg3));
    }
}