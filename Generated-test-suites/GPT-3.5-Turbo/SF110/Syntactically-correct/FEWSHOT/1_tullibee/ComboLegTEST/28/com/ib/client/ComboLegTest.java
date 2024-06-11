package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComboLegTest {
    @Test
    public void testConstructor_Default() {
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
    public void testConstructor_PartialParams() {
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
    public void testConstructor_AllParams() {
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
    public void testEquals_SameObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(true, comboLeg.equals(comboLeg));
    }

    @Test
    public void testEquals_NullObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(false, comboLeg.equals(null));
    }

    @Test
    public void testEquals_EqualComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        assertEquals(true, comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location2");
        assertEquals(false, comboLeg1.equals(comboLeg2));
    }
}