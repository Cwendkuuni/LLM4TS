package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructorWithParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructorWithDefaultParameters() {
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
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Location2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
}