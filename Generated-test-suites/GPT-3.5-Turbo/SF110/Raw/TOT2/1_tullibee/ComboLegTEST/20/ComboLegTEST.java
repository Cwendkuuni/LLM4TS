package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "NYC");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("NYC", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "NYC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "NYC");
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "SELL", "NYSE", ComboLeg.CLOSE, 1, "CHI");

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}