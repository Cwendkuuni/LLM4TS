package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(2, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        ComboLeg comboLeg5 = new ComboLeg(1, 1, "BUY", "NASDAQ", 1);
        ComboLeg comboLeg6 = new ComboLeg(1, 1, "BUY", "NYSE", 2);
        ComboLeg comboLeg7 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        comboLeg7.m_shortSaleSlot = 1;
        ComboLeg comboLeg8 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        comboLeg8.m_designatedLocation = "IB";
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
        assertFalse(comboLeg1.equals(comboLeg5));
        assertFalse(comboLeg1.equals(comboLeg6));
        assertFalse(comboLeg1.equals(comboLeg7));
        assertFalse(comboLeg1.equals(comboLeg8));
    }
}