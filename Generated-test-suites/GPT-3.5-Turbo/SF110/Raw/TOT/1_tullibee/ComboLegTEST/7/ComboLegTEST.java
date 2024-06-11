import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testDefaultConstructor() {
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
    public void testParameterizedConstructorWithoutOptionalParameters() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(ComboLeg.OPEN, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructorWithAllParameters() {
        ComboLeg leg = new ComboLeg(1, 2, "SELL", "NASDAQ", ComboLeg.CLOSE, 1, "USA");
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NASDAQ", leg.m_exchange);
        assertEquals(ComboLeg.CLOSE, leg.m_openClose);
        assertEquals(1, leg.m_shortSaleSlot);
        assertEquals("USA", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals_SameObject() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        assertTrue(leg.equals(leg));
    }
    
    @Test
    public void testEquals_NullObject() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        assertFalse(leg.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        assertFalse(leg.equals("BUY"));
    }
    
    @Test
    public void testEquals_DifferentConID() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.OPEN);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.OPEN);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentAction() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.OPEN);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentShortSaleSlot() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 1, "USA");
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN, 0, "Europe");
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_EqualObjects() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.OPEN);
        assertTrue(leg1.equals(leg2));
    }
    
}