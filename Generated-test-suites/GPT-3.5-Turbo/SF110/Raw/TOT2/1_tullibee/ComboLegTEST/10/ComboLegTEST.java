import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertNotNull(comboLeg);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "Location");
        assertTrue(comboLeg1.equals(comboLeg2));
        
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "SELL", "NYSE", 1, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg3));
        
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 0, "Location");
        assertFalse(comboLeg1.equals(comboLeg4));
    }

}