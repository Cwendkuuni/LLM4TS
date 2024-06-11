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
    public void testParameterizedConstructor1() {
        ComboLeg leg = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("Buy", leg.m_action);
        assertEquals("NSE", leg.m_exchange);
        assertEquals(ComboLeg.OPEN, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructor2() {
        ComboLeg leg = new ComboLeg(1, 2, "Sell", "BSE", ComboLeg.CLOSE, 1, "Location");
        
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("Sell", leg.m_action);
        assertEquals("BSE", leg.m_exchange);
        assertEquals(ComboLeg.CLOSE, leg.m_openClose);
        assertEquals(1, leg.m_shortSaleSlot);
        assertEquals("Location", leg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsSameObject() {
        ComboLeg leg = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        
        assertTrue(leg.equals(leg));
    }
    
    @Test
    public void testEqualsNullObject() {
        ComboLeg leg = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        
        assertFalse(leg.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClassObject() {
        ComboLeg leg = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        
        assertFalse(leg.equals("Buy,NSE,Open"));
    }
    
    @Test
    public void testEqualsDifferentConId() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(2, 2, "Buy", "NSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 3, "Buy", "NSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.CLOSE);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentAction() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Sell", "NSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Buy", "BSE", ComboLeg.OPEN);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN, 0, "Loc");
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsSameComboLeg() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsComboLegWithNullDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Buy", "NSE", ComboLeg.OPEN, 0, null);
        
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsComboLegWithNullAction() {
        ComboLeg leg1 = new ComboLeg(1, 2, null, "NSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, null, "NSE", ComboLeg.OPEN);
        
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsComboLegWithNullExchange() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Buy", null, ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "Buy", null, ComboLeg.OPEN);
        
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsComboLegWithNullActionAndExchange() {
        ComboLeg leg1 = new ComboLeg(1, 2, null, null, ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, null, null, ComboLeg.OPEN);

        assertTrue(leg1.equals(leg2));
    }
}