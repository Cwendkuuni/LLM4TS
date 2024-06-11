package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsMethod() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "Location");
        assertEquals(true, comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testNotEqualsMethod() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "Location");
        ComboLeg comboLeg2 = new ComboLeg(2, 1, "SELL", "NASDAQ", 2, 2, "Different Location");
        assertEquals(false, comboLeg1.equals(comboLeg2));
    }
}