package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals_sameObject() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(leg1.equals(leg1));
    }

    @Test
    public void testEquals_differentObjects() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(leg1.equals(leg2));
    }

    @Test
    public void testEquals_differentAction() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }

    @Test
    public void testEquals_nullObject() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertFalse(leg1.equals(null));
    }

}