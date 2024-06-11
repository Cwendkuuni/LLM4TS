package com.ib.client;

import static org.junit.Assert.*;
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
    public void testParameterizedConstructorWithoutShortSaleSlotAndDesignatedLocation() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        
        ComboLeg leg = new ComboLeg(conId, ratio, action, exchange, openClose);
        assertEquals(conId, leg.m_conId);
        assertEquals(ratio, leg.m_ratio);
        assertEquals(action, leg.m_action);
        assertEquals(exchange, leg.m_exchange);
        assertEquals(openClose, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructorWithAllParameters() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        int shortSaleSlot = 3;
        String designatedLocation = "Location";
        
        ComboLeg leg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        assertEquals(conId, leg.m_conId);
        assertEquals(ratio, leg.m_ratio);
        assertEquals(action, leg.m_action);
        assertEquals(exchange, leg.m_exchange);
        assertEquals(openClose, leg.m_openClose);
        assertEquals(shortSaleSlot, leg.m_shortSaleSlot);
        assertEquals(designatedLocation, leg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsWithSameObject() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(leg.equals(leg));
    }
    
    @Test
    public void testEqualsWithNull() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertFalse(leg.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentTypeObject() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertFalse(leg.equals("BUY"));
    }
    
    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 3, "BUY", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, null);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertFalse(leg1.equals(leg2));
    }
}