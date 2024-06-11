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
    public void testParameterizedConstructor() {
        ComboLeg leg = new ComboLeg(1, 2, "Action", "Exchange", 3);
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("Action", leg.m_action);
        assertEquals("Exchange", leg.m_exchange);
        assertEquals(3, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testFullyParameterizedConstructor() {
        ComboLeg leg = new ComboLeg(1, 2, "Action", "Exchange", 3, 4, "Location");
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("Action", leg.m_action);
        assertEquals("Exchange", leg.m_exchange);
        assertEquals(3, leg.m_openClose);
        assertEquals(4, leg.m_shortSaleSlot);
        assertEquals("Location", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Action", "Exchange", 3, 4, "Location");
        ComboLeg leg2 = new ComboLeg(1, 2, "Action", "Exchange", 3, 4, "Location");
        ComboLeg leg3 = new ComboLeg(2, 2, "Action", "Exchange", 3, 4, "Location");
        
        assertTrue(leg1.equals(leg2));
        assertTrue(leg2.equals(leg1));
        assertFalse(leg1.equals(leg3));
        assertFalse(leg3.equals(leg1));
    }
}