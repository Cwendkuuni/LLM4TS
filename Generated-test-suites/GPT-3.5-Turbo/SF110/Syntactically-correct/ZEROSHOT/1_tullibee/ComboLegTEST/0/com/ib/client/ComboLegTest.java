package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

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
        ComboLeg leg = new ComboLeg(1, 2, "action", "exchange", 3);
        
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("action", leg.m_action);
        assertEquals("exchange", leg.m_exchange);
        assertEquals(3, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testFullyParameterizedConstructor() {
        ComboLeg leg = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("action", leg.m_action);
        assertEquals("exchange", leg.m_exchange);
        assertEquals(3, leg.m_openClose);
        assertEquals(4, leg.m_shortSaleSlot);
        assertEquals("location", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg leg2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        
        assertEquals(leg1, leg2);
    }
    
    @Test
    public void testNotEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg leg2 = new ComboLeg(2, 2, "action", "exchange", 3, 4, "location");
        
        assertNotEquals(leg1, leg2);
    }
}