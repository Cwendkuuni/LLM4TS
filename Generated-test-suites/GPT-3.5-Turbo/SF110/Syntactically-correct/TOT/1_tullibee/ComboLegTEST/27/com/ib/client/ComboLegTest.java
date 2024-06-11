package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testPartialConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertNotNull(comboLeg);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testFullConstructor() {
        ComboLeg comboLeg = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 3, "Location");
        assertNotNull(comboLeg);
        assertEquals(3, comboLeg.m_conId);
        assertEquals(4, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 5, "Location");

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);

        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);

        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        String string = "BUY";

        assertFalse(comboLeg.equals(string));
    }

    @Test
    public void testEqualsDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1);

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", 1);

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", 1);

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2);

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "Location");

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location2");

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentActionIgnoringCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Location");

        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentExchangeIgnoringCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "nyse", 1, 2, "Location");

        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentDesignatedLocationIgnoringCase() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "location1");

        assertTrue(comboLeg1.equals(comboLeg2));
    }

}