package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsMethod() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsMethodNotEqual() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "SELL", "NASDAQ", 2);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}