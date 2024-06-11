import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testConstructorWithNoArg() {
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
    public void testConstructorWithFiveArgs() {
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
    public void testConstructorWithSevenArgs() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "LOC");

        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(2, leg.m_shortSaleSlot);
        assertEquals("LOC", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "LOC");
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "LOC");
        ComboLeg leg3 = new ComboLeg(2, 2, "BUY", "NYSE", 1, 2, "LOC");
        ComboLeg leg4 = new ComboLeg(1, 3, "BUY", "NYSE", 1, 2, "LOC");
        ComboLeg leg5 = new ComboLeg(1, 2, "SELL", "NYSE", 1, 2, "LOC");
        ComboLeg leg6 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 2, "LOC");
        ComboLeg leg7 = new ComboLeg(1, 2, "BUY", "NYSE", 2, 2, "LOC");
        ComboLeg leg8 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 3, "LOC");
        ComboLeg leg9 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 2, "LOC2");
        
        assertEquals(leg1, leg2);
        assertFalse(leg1.equals(leg3));
        assertFalse(leg1.equals(leg4));
        assertFalse(leg1.equals(leg5));
        assertFalse(leg1.equals(leg6));
        assertFalse(leg1.equals(leg7));
        assertFalse(leg1.equals(leg8));
        assertFalse(leg1.equals(leg9));
    }
}