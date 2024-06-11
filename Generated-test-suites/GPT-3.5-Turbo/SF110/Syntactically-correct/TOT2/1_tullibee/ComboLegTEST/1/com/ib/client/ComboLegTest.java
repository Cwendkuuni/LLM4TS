package com.ib.client;
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "NY");
        assertNotNull(comboLeg);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("NY", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "NY");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "NY");
        assertEquals(comboLeg1, comboLeg2);
        
        comboLeg2 = new ComboLeg(2, 3, "SELL", "NASDAQ", 2, 1, "LA");
        assertNotEquals(comboLeg1, comboLeg2);
    }
}