package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 0, "Location");
        ComboLeg comboLeg3 = new ComboLeg(2, 3, "Sell", "NASDAQ", 2, 1, "Other Location");

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}