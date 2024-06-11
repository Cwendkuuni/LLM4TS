package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testConstructor1() {
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
    public void testConstructor2() {
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
    public void testConstructor3() {
        ComboLeg leg = new ComboLeg(1, 2, "SELL", "NASDAQ", 2, 1, "Floor");
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NASDAQ", leg.m_exchange);
        assertEquals(2, leg.m_openClose);
        assertEquals(1, leg.m_shortSaleSlot);
        assertEquals("Floor", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(1, 2, "SELL", "NASDAQ", 2, 1, "Floor");
        
        // Test same object
        assertTrue(leg1.equals(leg1));
        
        // Test different object with same values
        assertTrue(leg1.equals(leg2));
        
        // Test different object with different values
        assertFalse(leg1.equals(leg3));
    }
}