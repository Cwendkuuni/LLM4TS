package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testDefaultConstructor() {
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
    public void testParameterizedConstructor1() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);
        
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructor2() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        int shortSaleSlot = 3;
        String designatedLocation = "LOC1";
        
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsSameValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NASDAQ", 2, 3, "LOC2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentActionCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 3, "LOC1");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentExchangeCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "Nyse", 1, 3, "LOC1");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentDesignatedLocationCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "Loc1");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", 1, 3, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 3, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 4, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentActionNull() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, null, "NYSE", 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentExchangeNull() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", null, 1, 3, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentDesignatedLocationNull() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, null);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}