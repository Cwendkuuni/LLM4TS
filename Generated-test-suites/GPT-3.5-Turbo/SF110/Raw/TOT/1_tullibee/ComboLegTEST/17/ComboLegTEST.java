
package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    private ComboLeg comboLeg1;
    private ComboLeg comboLeg2;

    @Before
    public void setUp() {
        comboLeg1 = new ComboLeg();
        comboLeg2 = new ComboLeg(1, 2, "test", "test exchange", 1, 2, "test location");
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, comboLeg1.m_conId);
        assertEquals(0, comboLeg1.m_ratio);
        assertNull(comboLeg1.m_action);
        assertNull(comboLeg1.m_exchange);
        assertEquals(0, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);
    }

    @Test
    public void testPartialConstructor() {
        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("test", comboLeg2.m_action);
        assertEquals("test exchange", comboLeg2.m_exchange);
        assertEquals(1, comboLeg2.m_openClose);
        assertEquals(2, comboLeg2.m_shortSaleSlot);
        assertEquals("test location", comboLeg2.m_designatedLocation);
    }

    @Test
    public void testFullConstructor() {
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "test", "test exchange", 1, 2, "test location");
        assertTrue(comboLeg2.equals(comboLeg3));
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "test", "test exchange", 1, 2, "test location");
        assertTrue(comboLeg2.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(0, 0, null, null, 0, 0, null);
        assertTrue(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(1, 2, "different action", "test exchange", 1, 2, "test location");
        assertFalse(comboLeg2.equals(comboLeg5));

        ComboLeg comboLeg6 = new ComboLeg(1, 2, "test", "different exchange", 1, 2, "test location");
        assertFalse(comboLeg2.equals(comboLeg6));

        ComboLeg comboLeg7 = new ComboLeg(1, 2, "test", "test exchange", 2, 2, "test location");
        assertFalse(comboLeg2.equals(comboLeg7));

        ComboLeg comboLeg8 = new ComboLeg(1, 2, "test", "test exchange", 1, 3, "test location");
        assertFalse(comboLeg2.equals(comboLeg8));

        ComboLeg comboLeg9 = new ComboLeg(1, 2, "test", "test exchange", 1, 2, "different location");
        assertFalse(comboLeg2.equals(comboLeg9));     
    }
}
