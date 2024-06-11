package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals_sameObject_returnsTrue() {
        ComboLeg comboLeg = new ComboLeg();
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEquals_nullObject_returnsFalse() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEquals_differentClass_returnsFalse() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals("test"));
    }

    @Test
    public void testEquals_equalComboLegs_returnsTrue() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentConId_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentRatio_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentAction_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.SAME, 1, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentExchange_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.SAME, 1, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentOpenClose_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentShortSaleSlot_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 2, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentDesignatedLocation_returnsFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "USA");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, "UK");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}