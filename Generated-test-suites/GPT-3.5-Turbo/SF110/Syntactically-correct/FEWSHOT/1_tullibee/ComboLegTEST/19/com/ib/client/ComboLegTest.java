package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testDefaultConstructor() {
        ComboLeg leg = new ComboLeg();
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertEquals(null, leg.m_action);
        assertEquals(null, leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertEquals(null, leg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithParams() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = 1;
        int shortSaleSlot = 0;
        String designatedLocation = "LOC";

        ComboLeg leg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(conId, leg.m_conId);
        assertEquals(ratio, leg.m_ratio);
        assertEquals(action, leg.m_action);
        assertEquals(exchange, leg.m_exchange);
        assertEquals(openClose, leg.m_openClose);
        assertEquals(shortSaleSlot, leg.m_shortSaleSlot);
        assertEquals(designatedLocation, leg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC");
        ComboLeg leg3 = new ComboLeg(2, 2, "SELL", "NASDAQ", 2, 0, "LOC");

        assertTrue(leg1.equals(leg2));
        assertTrue(!leg1.equals(leg3));
    }
}