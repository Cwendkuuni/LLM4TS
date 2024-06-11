
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testConstructor1() {
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
    public void testConstructor2() {
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
    public void testConstructor3() {
        ComboLeg leg = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 3, "Location");
        assertEquals(3, leg.m_conId);
        assertEquals(4, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NASDAQ", leg.m_exchange);
        assertEquals(2, leg.m_openClose);
        assertEquals(3, leg.m_shortSaleSlot);
        assertEquals("Location", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 3, "Location");
        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
    }
}
