import static org.junit.Assert.*;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {
    
    @Test
    public void testDefaultConstructor() {
        ComboLeg leg = new ComboLeg();
        
        // Check initial values
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
        ComboLeg leg = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        
        // Check assigned values
        assertEquals(123, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("BUY", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
    
    @Test
    public void testFullConstructor() {
        ComboLeg leg = new ComboLeg(456, 3, "SELL", "NASDAQ", 2, 3, "London");
        
        // Check assigned values
        assertEquals(456, leg.m_conId);
        assertEquals(3, leg.m_ratio);
        assertEquals("SELL", leg.m_action);
        assertEquals("NASDAQ", leg.m_exchange);
        assertEquals(2, leg.m_openClose);
        assertEquals(3, leg.m_shortSaleSlot);
        assertEquals("London", leg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(456, 3, "SELL", "NASDAQ", 2, 3, "London");
        
        // Check equality
        assertEquals(leg1, leg2);
        
        // Check inequality
        assertNotEquals(leg1, leg3);
    }
}