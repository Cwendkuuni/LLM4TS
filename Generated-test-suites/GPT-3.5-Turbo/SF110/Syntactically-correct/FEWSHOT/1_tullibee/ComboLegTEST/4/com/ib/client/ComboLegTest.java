package com.ib.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testConstructorNoArgs() {
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
    public void testConstructorWithArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithFullArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "LOC");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_shortSaleSlot);
        assertEquals("LOC", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(3, 4, "SELL", "NASDAQ", 2);
        
        assertEquals(true, comboLeg1.equals(comboLeg1));
        assertEquals(true, comboLeg1.equals(comboLeg2));
        assertEquals(false, comboLeg1.equals(comboLeg3));
    }
}