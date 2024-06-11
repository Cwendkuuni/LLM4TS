import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructorDefault() {
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
    public void testComboLegConstructorWithParams() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructorWithAllParams() {
        ComboLeg leg = new ComboLeg(1, 2, "SELL", "NYSE", 2, 3, "Location");
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(2, leg.m_openClose);
        assertEquals(3, leg.m_shortSaleSlot);
        assertEquals("Location", leg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsSameObject() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(leg.equals(leg));
    }
    
    @Test
    public void testEqualsNullObject() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertFalse(leg.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertFalse(leg.equals("Test"));
    }
    
    @Test
    public void testEqualsSameAttributes() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentConId() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(2, 2, "BUY", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 3, "BUY", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentAction() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "SELL", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 2);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, null);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertFalse(leg1.equals(leg2));
    }
}