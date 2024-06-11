
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructor() {
        // Test default constructor
        ComboLeg cl1 = new ComboLeg();
        assertEquals(0, cl1.m_conId);
        assertEquals(0, cl1.m_ratio);
        assertNull(cl1.m_action);
        assertNull(cl1.m_exchange);
        assertEquals(0, cl1.m_openClose);
        assertEquals(0, cl1.m_shortSaleSlot);
        assertNull(cl1.m_designatedLocation);
        
        // Test constructor with arguments
        ComboLeg cl2 = new ComboLeg(1, 2, "action", "exchange", 3);
        assertEquals(1, cl2.m_conId);
        assertEquals(2, cl2.m_ratio);
        assertEquals("action", cl2.m_action);
        assertEquals("exchange", cl2.m_exchange);
        assertEquals(3, cl2.m_openClose);
        assertEquals(0, cl2.m_shortSaleSlot);
        assertNull(cl2.m_designatedLocation);
        
        // Test constructor with all arguments
        ComboLeg cl3 = new ComboLeg(4, 5, "buy", "nyse", 6, 7, "location");
        assertEquals(4, cl3.m_conId);
        assertEquals(5, cl3.m_ratio);
        assertEquals("buy", cl3.m_action);
        assertEquals("nyse", cl3.m_exchange);
        assertEquals(6, cl3.m_openClose);
        assertEquals(7, cl3.m_shortSaleSlot);
        assertEquals("location", cl3.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "action", "exchange", 3);
        ComboLeg cl2 = new ComboLeg(1, 2, "action", "exchange", 3);
        ComboLeg cl3 = new ComboLeg(4, 5, "buy", "nyse", 6, 7, "location");
        
        // Test equal objects
        assertTrue(cl1.equals(cl2));
        assertTrue(cl2.equals(cl1));
        
        // Test unequal objects
        assertFalse(cl1.equals(cl3));
        assertFalse(cl3.equals(cl1));
    }
}
