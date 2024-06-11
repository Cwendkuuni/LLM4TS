
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor1() {
        ComboLeg leg = new ComboLeg();
        
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertNull(leg.m_action);
        assertNull(leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructor2() {
        ComboLeg leg = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        
        assertEquals(1001, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructor3() {
        ComboLeg leg = new ComboLeg(2001, 5, "SELL", "NASDAQ", 2, 1, "Location XYZ");
        
        assertEquals(2001, leg.m_conId);
        assertEquals(5, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NASDAQ", leg.m_exchange);
        assertEquals(2, leg.m_openClose);
        assertEquals(1, leg.m_shortSaleSlot);
        assertEquals("Location XYZ", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals_SameObject() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        
        assertTrue(leg1.equals(leg1));
    }
    
    @Test
    public void testEquals_NullObject() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        
        assertFalse(leg1.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        Object leg2 = new Object();
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_SameValues() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentConId() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(2001, 2, "BUY", "NYSE", 1);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 3, "BUY", "NYSE", 1);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentAction() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 2, "SELL", "NYSE", 1);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 2, "BUY", "NASDAQ", 1);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 2, "BUY", "NYSE", 2);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentShortSaleSlot() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 2, "BUY", "NYSE", 1, 1, null);
        
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1001, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1001, 2, "BUY", "NYSE", 1, 0, "Location XYZ");
        
        assertFalse(leg1.equals(leg2));
    }
}

