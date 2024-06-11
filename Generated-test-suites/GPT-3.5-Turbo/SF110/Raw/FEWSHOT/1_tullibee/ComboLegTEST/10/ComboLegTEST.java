package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg1 = new ComboLeg();
        assertEquals(0, comboLeg1.m_conId);
        assertEquals(0, comboLeg1.m_ratio);
        assertEquals(null, comboLeg1.m_action);
        assertEquals(null, comboLeg1.m_exchange);
        assertEquals(0, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertEquals(null, comboLeg1.m_designatedLocation);
        
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("Buy", comboLeg2.m_action);
        assertEquals("NYSE", comboLeg2.m_exchange);
        assertEquals(1, comboLeg2.m_openClose);
        assertEquals(0, comboLeg2.m_shortSaleSlot);
        assertEquals(null, comboLeg2.m_designatedLocation);
        
        ComboLeg comboLeg3 = new ComboLeg(3, 4, "Sell", "NASDAQ", 2, 3, "New York");
        assertEquals(3, comboLeg3.m_conId);
        assertEquals(4, comboLeg3.m_ratio);
        assertEquals("Sell", comboLeg3.m_action);
        assertEquals("NASDAQ", comboLeg3.m_exchange);
        assertEquals(2, comboLeg3.m_openClose);
        assertEquals(3, comboLeg3.m_shortSaleSlot);
        assertEquals("New York", comboLeg3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertEquals(true, comboLeg1.equals(comboLeg2));
        
        ComboLeg comboLeg3 = new ComboLeg(3, 4, "Sell", "NASDAQ", 2, 3, "New York");
        ComboLeg comboLeg4 = new ComboLeg(3, 4, "Sell", "NASDAQ", 2, 3, "New York");
        assertEquals(true, comboLeg3.equals(comboLeg4));
        
        ComboLeg comboLeg5 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        ComboLeg comboLeg6 = new ComboLeg(2, 2, "Buy", "NYSE", 1);
        assertEquals(false, comboLeg5.equals(comboLeg6));
        
        ComboLeg comboLeg7 = new ComboLeg(3, 4, "Sell", "NASDAQ", 2, 3, "New York");
        ComboLeg comboLeg8 = new ComboLeg(3, 4, "Buy", "NASDAQ", 2, 3, "New York");
        assertEquals(false, comboLeg7.equals(comboLeg8));
        
        ComboLeg comboLeg9 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        ComboLeg comboLeg10 = null;
        assertEquals(false, comboLeg9.equals(comboLeg10));
    }

}