import static org.junit.Assert.*;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {
    
    @Test
    public void testComboLegConstructorWithNoParameters() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructorWithParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertNotNull(comboLeg);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testComboLegConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "ABC");
        assertNotNull(comboLeg);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals("ABC", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "ABC");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "ABC");
        ComboLeg comboLeg3 = new ComboLeg(2, 3, "SELL", "NASDAQ", 0, 1, "XYZ");
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg2.equals(comboLeg3));
    }
}