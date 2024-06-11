package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
    public void testPartialConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", 3);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action", comboLeg.m_action);
        assertEquals("Exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testFullConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", 3, 4, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action", comboLeg.m_action);
        assertEquals("Exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(4, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", 3);
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", 3);
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", 3);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "Action", "Exchange", 3);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsSameFields() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", 3);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", 3);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentFields() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", 3);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "DifferentAction", "Exchange", 3);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}