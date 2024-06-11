

import com.ib.client.ComboLeg;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Buy", "Exchange1", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "Buy", "Exchange1", 1);
        ComboLeg leg3 = new ComboLeg(2, 1, "Sell", "Exchange2", 2);

        // Test equal objects
        assertTrue(leg1.equals(leg1));
        assertTrue(leg1.equals(leg2));
        assertTrue(leg2.equals(leg1));

        // Test unequal objects
        assertFalse(leg1.equals(null));
        assertFalse(leg1.equals(leg3));
        assertFalse(leg3.equals(leg1));
    }

    @Test
    public void testConstructor() {
        ComboLeg leg = new ComboLeg(1, 1, "Buy", "Exchange1", 1, 2, "Location");
        assertEquals(1, leg.m_conId);
        assertEquals(1, leg.m_ratio);
        assertEquals("Buy", leg.m_action);
        assertEquals("Exchange1", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(2, leg.m_shortSaleSlot);
        assertEquals("Location", leg.m_designatedLocation);
    }
    
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
        ComboLeg leg = new ComboLeg(1, 1, "Buy", "Exchange1", 1);
        assertEquals(1, leg.m_conId);
        assertEquals(1, leg.m_ratio);
        assertEquals("Buy", leg.m_action);
        assertEquals("Exchange1", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }
}
