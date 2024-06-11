package com.ib.client;
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
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
    public void testComboLegParameterizedConstructor() {
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
    public void testComboLegEqualsSameObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "SELL", "NASDAQ", 2);
        assertTrue(comboLeg1.equals(comboLeg1));
    }
    
    @Test
    public void testComboLegEqualsDifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "SELL", "NASDAQ", 2);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "SELL", "NASDAQ", 2);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testComboLegNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "SELL", "NASDAQ", 2);
        ComboLeg comboLeg2 = new ComboLeg(2, 1, "SELL", "NASDAQ", 2);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}