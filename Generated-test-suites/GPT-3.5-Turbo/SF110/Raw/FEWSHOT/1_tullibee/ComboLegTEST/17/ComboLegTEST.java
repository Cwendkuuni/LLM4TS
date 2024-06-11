package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

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
    public void testParameterizedConstructor1() {
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
    public void testParameterizedConstructor2() {
        ComboLeg comboLeg = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 3, "Location1");
        
        assertEquals(3, comboLeg.m_conId);
        assertEquals(4, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("Location1", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg4 = new ComboLeg(1, 3, "BUY", "NYSE", 1);
        ComboLeg comboLeg5 = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        ComboLeg comboLeg6 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg7 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg comboLeg8 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location1");
        ComboLeg comboLeg9 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location2");
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
        assertFalse(comboLeg1.equals(comboLeg5));
        assertFalse(comboLeg1.equals(comboLeg6));
        assertFalse(comboLeg1.equals(comboLeg7));
        assertFalse(comboLeg1.equals(comboLeg8));
        assertFalse(comboLeg8.equals(comboLeg9));
    }
}