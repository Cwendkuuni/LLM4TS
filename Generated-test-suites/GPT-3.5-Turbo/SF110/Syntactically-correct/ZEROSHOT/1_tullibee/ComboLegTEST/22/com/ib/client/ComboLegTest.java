package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(null, comboLeg.m_action);
        assertEquals(null, comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testParamConstructor1() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testParamConstructor2() {
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
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        
        assertEquals(true, comboLeg1.equals(comboLeg1));
        assertEquals(true, comboLeg1.equals(comboLeg2));
        assertEquals(false, comboLeg1.equals(comboLeg3));
        assertEquals(false, comboLeg1.equals(null));
    }
}