package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ComboLegTest {

    private ComboLeg comboLeg;
    
    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }
    
    @Test
    public void testComboLegConstructor() {
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegParameterizedConstructorNoShortSaleSlot() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "buy", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegParameterizedConstructorWithShortSaleSlot() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "sell", "NASDAQ", 2, 1, "A");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("sell", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("A", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsSameObject() {
        assertEquals(comboLeg, comboLeg);
    }
    
    @Test
    public void testEqualsNullObject() {
        assertNotEquals(comboLeg, null);
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertNotEquals(comboLeg, "comboLeg");
    }
    
    @Test
    public void testEqualsEqualComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "buy", "NYSE", 1);
        assertEquals(comboLeg1, comboLeg2);
    }
    
    @Test
    public void testEqualsNotEqualComboLegs() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "sell", "NASDAQ", 2);
        assertNotEquals(comboLeg1, comboLeg2);
    }

}