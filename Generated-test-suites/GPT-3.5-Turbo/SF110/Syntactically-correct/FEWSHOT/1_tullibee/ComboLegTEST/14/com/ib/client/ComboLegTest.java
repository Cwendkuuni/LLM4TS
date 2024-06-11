package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        assertTrue(leg1.equals(leg2));
        
        ComboLeg leg3 = new ComboLeg(1, 1, "SELL", "NASDAQ", 2);
        assertFalse(leg1.equals(leg3));
        
        ComboLeg leg4 = new ComboLeg(1, 1, "SELL", "NYSE", 1);
        assertFalse(leg1.equals(leg4));
    }
    
    @Test
    public void testDefaultConstructor() {
        ComboLeg leg = new ComboLeg();
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertEquals(null, leg.m_action);
        assertEquals(null, leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertEquals(null, leg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithParameters() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "LOC");
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(2, leg.m_shortSaleSlot);
        assertEquals("LOC", leg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithPartialParameters() {
        ComboLeg leg = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertEquals(null, leg.m_designatedLocation);
    }
}