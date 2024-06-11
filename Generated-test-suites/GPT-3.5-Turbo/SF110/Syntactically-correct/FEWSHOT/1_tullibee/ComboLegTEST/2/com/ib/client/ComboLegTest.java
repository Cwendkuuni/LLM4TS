package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ComboLegTest {
    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();

        // Verify that all fields are initialized correctly
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(null, comboLeg.m_action);
        assertEquals(null, comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithFiveParameters() {
        int conId = 123;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.CLOSE;

        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);

        // Verify that all fields are initialized correctly
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testConstructorWithSevenParameters() {
        int conId = 123;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.CLOSE;
        int shortSaleSlot = 1;
        String designatedLocation = "LOC1";

        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        // Verify that all fields are initialized correctly
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals_SameObject() {
        ComboLeg comboLeg = new ComboLeg();
        
        // Verify that the object is equal to itself
        assertEquals(comboLeg, comboLeg);
    }
    
    @Test
    public void testEquals_NullObject() {
        ComboLeg comboLeg = new ComboLeg();
        
        // Verify that the object is not equal to null
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEquals_DifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg();
        comboLeg2.m_conId = 123;
        
        // Verify that the objects are not equal
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_EqualObjects() {
        int conId = 123;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NYSE";
        int openClose = ComboLeg.CLOSE;
        int shortSaleSlot = 1;
        String designatedLocation = "LOC1";

        ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        // Verify that the objects are equal
        assertEquals(comboLeg1, comboLeg2);
    }
}