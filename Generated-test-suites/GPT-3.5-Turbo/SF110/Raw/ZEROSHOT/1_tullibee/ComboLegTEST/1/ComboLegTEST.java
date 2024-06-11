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
    public void testParamConstructorWithFiveArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testParamConstructorWithSevenArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Location");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Sell", "NYSE", 1, 2, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}