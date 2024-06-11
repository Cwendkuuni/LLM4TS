package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testConstructorNoArgs() {
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
    public void testConstructorWithArgs5() {
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
    public void testConstructorWithArgs7() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        int shortSaleSlot = 2;
        String designatedLocation = "USA";
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
        ComboLeg comboLeg = new ComboLeg();
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsNull() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ComboLeg comboLeg = new ComboLeg();
        String str = "test";
        assertFalse(comboLeg.equals(str));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 0);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", 0);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", 0);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 0);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 0, 1, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0, 0, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 0, 0, "CAN");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}