import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, null);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    // Basic test for ComboLeg constructor with no arguments
    @Test
    public void testComboLegNoArgs() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    // Basic test for ComboLeg constructor with arguments excluding shortSaleSlot and designatedLocation
    @Test
    public void testComboLegArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    // Basic test for ComboLeg constructor with all arguments
    @Test
    public void testComboLegAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOCATION");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("LOCATION", comboLeg.m_designatedLocation);
    }
    
}