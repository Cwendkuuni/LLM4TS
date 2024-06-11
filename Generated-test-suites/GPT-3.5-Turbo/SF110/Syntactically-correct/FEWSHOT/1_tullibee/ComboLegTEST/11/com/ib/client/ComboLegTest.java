package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Util;

public class ComboLegTest {

    @Test
    public void testComboLeg() {
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
    public void testComboLegWithParams() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegWithAllParams() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(4, comboLeg.m_shortSaleSlot);
        assertEquals("location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg comboLeg2 = new ComboLeg(2, 3, "action", "exchange", 3, 4, "location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}