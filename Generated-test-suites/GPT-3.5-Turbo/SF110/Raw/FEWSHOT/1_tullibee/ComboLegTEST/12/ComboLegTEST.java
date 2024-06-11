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
        int p_conId = 1;
        int p_ratio = 2;
        String p_action = "BUY";
        String p_exchange = "NYSE";
        int p_openClose = 1;
        
        ComboLeg leg = new ComboLeg(p_conId, p_ratio, p_action, p_exchange, p_openClose);
        assertEquals(p_conId, leg.m_conId);
        assertEquals(p_ratio, leg.m_ratio);
        assertEquals(p_action, leg.m_action);
        assertEquals(p_exchange, leg.m_exchange);
        assertEquals(p_openClose, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testConstructor3() {
        int p_conId = 1;
        int p_ratio = 2;
        String p_action = "BUY";
        String p_exchange = "NYSE";
        int p_openClose = 1;
        int p_shortSaleSlot = 0;
        String p_designatedLocation = "LOCATION";
        
        ComboLeg leg = new ComboLeg(p_conId, p_ratio, p_action, p_exchange, p_openClose, p_shortSaleSlot, p_designatedLocation);
        assertEquals(p_conId, leg.m_conId);
        assertEquals(p_ratio, leg.m_ratio);
        assertEquals(p_action, leg.m_action);
        assertEquals(p_exchange, leg.m_exchange);
        assertEquals(p_openClose, leg.m_openClose);
        assertEquals(p_shortSaleSlot, leg.m_shortSaleSlot);
        assertEquals(p_designatedLocation, leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOCATION");
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOCATION");
        ComboLeg leg3 = new ComboLeg(2, 3, "SELL", "NASDAQ", 2, 1, "LOC");
        
        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
    }
}