package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg();
        ComboLeg leg2 = new ComboLeg();
        assertTrue(leg1.equals(leg2));

        leg1.m_action = "BUY";
        assertFalse(leg1.equals(leg2));
        leg2.m_action = "BUY";
        assertTrue(leg1.equals(leg2));

        leg1.m_conId = 1;
        assertFalse(leg1.equals(leg2));
        leg2.m_conId = 1;
        assertTrue(leg1.equals(leg2));

        leg1.m_designatedLocation = "USA";
        assertFalse(leg1.equals(leg2));
        leg2.m_designatedLocation = "USA";
        assertTrue(leg1.equals(leg2));

        leg1.m_exchange = "NYSE";
        assertFalse(leg1.equals(leg2));
        leg2.m_exchange = "NYSE";
        assertTrue(leg1.equals(leg2));

        leg1.m_openClose = ComboLeg.OPEN;
        assertFalse(leg1.equals(leg2));
        leg2.m_openClose = ComboLeg.OPEN;
        assertTrue(leg1.equals(leg2));

        leg1.m_ratio = 2;
        assertFalse(leg1.equals(leg2));
        leg2.m_ratio = 2;
        assertTrue(leg1.equals(leg2));

        leg1.m_shortSaleSlot = 3;
        assertFalse(leg1.equals(leg2));
        leg2.m_shortSaleSlot = 3;
        assertTrue(leg1.equals(leg2));

        leg1.m_conId = 4;
        assertFalse(leg1.equals(leg2));
        leg2.m_conId = 4;
        assertTrue(leg1.equals(leg2));
    }
}