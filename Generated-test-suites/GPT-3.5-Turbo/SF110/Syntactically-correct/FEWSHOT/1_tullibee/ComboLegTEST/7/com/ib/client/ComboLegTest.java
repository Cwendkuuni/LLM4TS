package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testDefaultConstructor() {
        ComboLeg leg = new ComboLeg();
        
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertNull(leg.m_action);
        assertNull(leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructor() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testFullyParameterizedConstructor() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "US");
        
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(2, leg.m_shortSaleSlot);
        assertEquals("US", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(2, 2, "SELL", "NYSE", 2);
        
        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
    }
    
}