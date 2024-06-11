
package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testComboLeg() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegWithParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 2);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("LOC1", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        assertFalse(comboLeg.equals("different"));
    }

    @Test
    public void testEqualsDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 2, 1, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", 2, 1, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 2, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", 2, 1, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 2, 1, "LOC1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsEqualComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 1, "LOC1");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
}

