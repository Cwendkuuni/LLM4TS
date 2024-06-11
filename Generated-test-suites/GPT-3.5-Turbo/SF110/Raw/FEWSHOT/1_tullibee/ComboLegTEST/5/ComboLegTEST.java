package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor1() {
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
    public void testConstructor2() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertEquals(null, leg.m_designatedLocation);
    }

    @Test
    public void testConstructor3() {
        ComboLeg leg = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 1, "LOC");
        assertEquals(3, leg.m_conId);
        assertEquals(4, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NASDAQ", leg.m_exchange);
        assertEquals(2, leg.m_openClose);
        assertEquals(1, leg.m_shortSaleSlot);
        assertEquals("LOC", leg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 1, "LOC");

        assertTrue(leg1.equals(leg2));
        assertTrue(leg2.equals(leg1));
        assertFalse(leg1.equals(leg3));
        assertFalse(leg3.equals(leg1));
    }
}