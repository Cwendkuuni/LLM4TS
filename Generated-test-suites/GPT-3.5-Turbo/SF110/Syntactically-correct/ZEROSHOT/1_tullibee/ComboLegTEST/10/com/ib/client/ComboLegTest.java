package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testConstructorWithNoArguments() {
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
    public void testConstructorWithFiveArguments() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.OPEN;
        
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
    public void testConstructorWithAllArguments() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 3;
        String designatedLocation = "H1";
        
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
    public void testEqualsReturnTrueForSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertEquals(comboLeg, comboLeg);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForNull() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertNotEquals(comboLeg, null);
    }
    
    @Test
    public void testEqualsReturnTrueForEqualComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 3, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 4, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN, 3, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN, 3, "H1");
        assertNotEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsReturnFalseForDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 3, "H2");
        assertNotEquals(comboLeg1, comboLeg2);
    }

}