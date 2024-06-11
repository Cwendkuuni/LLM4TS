package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 1, "BUY", "NASDAQ", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 1, "BUY", "NASDAQ", ComboLeg.OPEN);
        ComboLeg leg3 = new ComboLeg(2, 1, "SELL", "NYSE", ComboLeg.CLOSE);

        // Test when leg1 is equal to leg2
        assertTrue(leg1.equals(leg2));
        assertTrue(leg2.equals(leg1));

        // Test when leg1 is not equal to leg3
        assertFalse(leg1.equals(leg3));
        assertFalse(leg3.equals(leg1));

        // Test when comparing a leg to null
        assertFalse(leg1.equals(null));
    }

    @Test
    public void testConstructor() {
        ComboLeg leg = new ComboLeg(1, 1, "SELL", "NYSE", ComboLeg.CLOSE, 0, "Location");

        // Test that the properties are set correctly
        assertEquals(1, leg.m_conId);
        assertEquals(1, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(ComboLeg.CLOSE, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertEquals("Location", leg.m_designatedLocation);
    }

    @Test
    public void testDefaultConstructor() {
        ComboLeg leg = new ComboLeg();

        // Test that the default properties are set correctly
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertNull(leg.m_action);
        assertNull(leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
}


