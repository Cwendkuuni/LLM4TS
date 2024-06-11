import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
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
    public void testComboLegParameterizedConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegParameterizedConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        String other = "other";
        
        assertFalse(comboLeg.equals(other));
    }
    
    @Test
    public void testEqualsEqualComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1, 2, "Location");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", 1, 2, "Location");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", 1, 2, "Location");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 2, "Location");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 2, "Location");
        
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
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "Location2");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}