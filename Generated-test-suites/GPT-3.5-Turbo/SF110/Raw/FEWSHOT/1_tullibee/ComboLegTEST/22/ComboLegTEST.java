package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor1() {
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
    public void testComboLegConstructor2() {
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
    public void testComboLegConstructor3() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "SELL", "NASDAQ", 2, 3, "LOC");

        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("LOC", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NASDAQ", 2, 3, "LOC");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "SELL", "NASDAQ", 2, 3, "LOC");

        assertTrue(comboLeg2.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}