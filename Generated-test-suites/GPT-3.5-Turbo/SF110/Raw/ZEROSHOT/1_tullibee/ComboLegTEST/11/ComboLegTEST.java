package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorZeroArgs() {
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
    public void testConstructorFiveArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorSevenArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(4, comboLeg.m_shortSaleSlot);
        assertEquals("location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3);
        ComboLeg comboLeg2 = new ComboLeg(4, 5, "other", "otherExchange", 6);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsEqualValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

}