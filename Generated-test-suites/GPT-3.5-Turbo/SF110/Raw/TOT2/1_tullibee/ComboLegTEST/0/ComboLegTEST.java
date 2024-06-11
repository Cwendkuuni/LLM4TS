package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertNotNull(comboLeg);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg2 = new ComboLeg(2, 3, "SELL", "NYSE", 2);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testNullInputEquals() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testSameObjectEquals() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertTrue(comboLeg.equals(comboLeg));
    }

}