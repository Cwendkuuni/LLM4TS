
package com.ib.client;

import static org.junit.Assert.*;

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
    public void testParameterizedConstructorWithoutShortSaleSlotAndDesignatedLocation() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 0);
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 0, 3, "location");
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 0, 3, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 0, 3, "location");
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "action", "exchange", 0, 3, "location");
        ComboLeg comboLeg4 = new ComboLeg(1, 3, "action", "exchange", 0, 3, "location");
        ComboLeg comboLeg5 = new ComboLeg(1, 2, "ACTION", "exchange", 0, 3, "location");
        ComboLeg comboLeg6 = new ComboLeg(1, 2, "action", "EXCHANGE", 0, 3, "location");
        ComboLeg comboLeg7 = new ComboLeg(1, 2, "action", "exchange", 1, 3, "location");
        ComboLeg comboLeg8 = new ComboLeg(1, 2, "action", "exchange", 0, 4, "location");
        ComboLeg comboLeg9 = new ComboLeg(1, 2, "action", "exchange", 0, 3, "LOCATION");
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
        assertFalse(comboLeg1.equals(comboLeg5));
        assertFalse(comboLeg1.equals(comboLeg6));
        assertFalse(comboLeg1.equals(comboLeg7));
        assertFalse(comboLeg1.equals(comboLeg8));
        assertFalse(comboLeg1.equals(comboLeg9));
    }
    
}
