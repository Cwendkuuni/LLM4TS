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
    public void testComboLegPartialConstructor() {
        ComboLeg comboLeg = new ComboLeg(123, 2, "BUY", "NYSE", 1);
        assertEquals(123, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegCompleteConstructor() {
        ComboLeg comboLeg = new ComboLeg(456, 1, "SELL", "NASDAQ", 2, 5, "Location");
        assertEquals(456, comboLeg.m_conId);
        assertEquals(1, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(5, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(123, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg3 = new ComboLeg(456, 1, "SELL", "NASDAQ", 2);
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertTrue(comboLeg2.equals(comboLeg1));
        assertFalse(comboLeg3.equals(comboLeg2));
    }
    
}