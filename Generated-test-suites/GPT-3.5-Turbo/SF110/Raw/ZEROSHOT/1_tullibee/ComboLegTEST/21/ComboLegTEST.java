package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "SELL", "NASDAQ", 1);

        Assert.assertTrue(comboLeg1.equals(comboLeg2));
        Assert.assertFalse(comboLeg1.equals(comboLeg3));
    }
}