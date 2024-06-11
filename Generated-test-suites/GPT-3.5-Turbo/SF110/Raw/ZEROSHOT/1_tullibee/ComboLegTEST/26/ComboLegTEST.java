package com.ib.client;

import static org.junit.Assert.*;
import org.junit.*;

public class ComboLegTest {

    ComboLeg comboLeg;

    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor1() {
        comboLeg = new ComboLeg(1, 2, "action1", "exchange1", 1);

        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action1", comboLeg.m_action);
        assertEquals("exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor2() {
        comboLeg = new ComboLeg(3, 4, "action2", "exchange2", 2, 3, "location");

        assertEquals(3, comboLeg.m_conId);
        assertEquals(4, comboLeg.m_ratio);
        assertEquals("action2", comboLeg.m_action);
        assertEquals("exchange2", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(comboLeg.equals(new Object()));
    }

    @Test
    public void testEqualsEqualComboLeg() {
        ComboLeg other = new ComboLeg();
        assertTrue(comboLeg.equals(other));
    }

    @Test
    public void testEqualsNonEqualComboLeg() {
        ComboLeg other = new ComboLeg(1, 2, "action", "exchange", 1);
        assertFalse(comboLeg.equals(other));
    }

}