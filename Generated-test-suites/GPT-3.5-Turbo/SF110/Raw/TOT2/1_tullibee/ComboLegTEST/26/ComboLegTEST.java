import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testComboLegParameterizedConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "NY");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("NY", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "NY");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "NY");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testComboLegNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 1, "NY");
        ComboLeg comboLeg2 = new ComboLeg(2, 3, "SELL", "NYSE", 2, 2, "LA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}