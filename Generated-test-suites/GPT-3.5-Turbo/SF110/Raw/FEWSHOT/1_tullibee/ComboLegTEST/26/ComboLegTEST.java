package com.ib.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(null, comboLeg.m_action);
        assertEquals(null, comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1, 3, "Loc");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("Loc", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 3, "Loc");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 3, "Loc");
        ComboLeg comboLeg3 = new ComboLeg(2, 3, "Sell", "NASDAQ", 2, 4, "Location");
        
        assertEquals(true, comboLeg1.equals(comboLeg1));
        assertEquals(true, comboLeg1.equals(comboLeg2));
        assertEquals(false, comboLeg1.equals(comboLeg3));
    }
}