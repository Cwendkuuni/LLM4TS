package com.ib.client;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals_SameObject_ShouldReturnTrue() {
        ComboLeg c = new ComboLeg();
        assertEquals(c.equals(c), true);
    }

    @Test
    public void testEquals_NullObject_ShouldReturnFalse() {
        ComboLeg c = new ComboLeg();
        assertEquals(c.equals(null), false);
    }

    @Test
    public void testEquals_DifferentObject_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg();
        ComboLeg c2 = new ComboLeg();
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_IdenticalComboLegs_ShouldReturnTrue() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        assertEquals(c1.equals(c2), true);
    }

    @Test
    public void testEquals_DifferentConId_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(2, 2, "action", "exchange", 3, 4, "location");
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_DifferentRatio_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(1, 3, "action", "exchange", 3, 4, "location");
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_DifferentAction_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action1", "exchange", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(1, 2, "action2", "exchange", 3, 4, "location");
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_DifferentExchange_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange1", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(1, 2, "action", "exchange2", 3, 4, "location");
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_DifferentOpenClose_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(1, 2, "action", "exchange", 4, 4, "location");
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_DifferentShortSaleSlot_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg c2 = new ComboLeg(1, 2, "action", "exchange", 3, 5, "location");
        assertEquals(c1.equals(c2), false);
    }

    @Test
    public void testEquals_DifferentDesignatedLocation_ShouldReturnFalse() {
        ComboLeg c1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location1");
        ComboLeg c2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location2");
        assertEquals(c1.equals(c2), false);
    }
}