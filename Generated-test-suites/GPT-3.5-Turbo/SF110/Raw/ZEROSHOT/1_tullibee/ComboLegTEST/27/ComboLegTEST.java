package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 0, 0, "location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 0, 0, "location1");
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "action1", "exchange1", 0, 0, "location1");
        ComboLeg comboLeg4 = new ComboLeg(1, 3, "action1", "exchange1", 0, 0, "location1");

        // Test for equality with itself
        assertTrue(comboLeg1.equals(comboLeg1));

        // Test for equality with another object of the same class
        assertTrue(comboLeg1.equals(comboLeg2));

        // Test for inequality with different m_conId
        assertFalse(comboLeg1.equals(comboLeg3));

        // Test for inequality with different m_ratio
        assertFalse(comboLeg1.equals(comboLeg4));

        // Test for inequality with null object
        assertFalse(comboLeg1.equals(null));

        // Test for inequality with different object type
        assertFalse(comboLeg1.equals(new String("test")));
    }

    @Test
    public void testComboLegDefaultConstructor() {
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
    public void testComboLegParameterizedConstructor1() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action1", "exchange1", 0);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action1", comboLeg.m_action);
        assertEquals("exchange1", comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegParameterizedConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action1", "exchange1", 0, 0, "location1");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action1", comboLeg.m_action);
        assertEquals("exchange1", comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("location1", comboLeg.m_designatedLocation);
    }
}