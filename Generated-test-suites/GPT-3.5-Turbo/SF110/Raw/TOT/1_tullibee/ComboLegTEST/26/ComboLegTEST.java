import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    private ComboLeg comboLeg;
    
    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }
    
    // Test constructor ComboLeg()
    @Test
    public void testConstructor1() {
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    // Test constructor ComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange, int p_openClose)
    @Test
    public void testConstructor2() {
        ComboLeg comboLeg1 = new ComboLeg(1234, 1, "BUY", "NYSE", 1);
        assertEquals(1234, comboLeg1.m_conId);
        assertEquals(1, comboLeg1.m_ratio);
        assertEquals("BUY", comboLeg1.m_action);
        assertEquals("NYSE", comboLeg1.m_exchange);
        assertEquals(1, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);
    }
    
    // Test constructor ComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange, int p_openClose, int p_shortSaleSlot, String p_designatedLocation)
    @Test
    public void testConstructor3() {
        ComboLeg comboLeg2 = new ComboLeg(5678, 2, "SELL", "NASDAQ", 2, 3, "Location");
        assertEquals(5678, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("SELL", comboLeg2.m_action);
        assertEquals("NASDAQ", comboLeg2.m_exchange);
        assertEquals(2, comboLeg2.m_openClose);
        assertEquals(3, comboLeg2.m_shortSaleSlot);
        assertEquals("Location", comboLeg2.m_designatedLocation);
    }
    
    // Test equals() method
    @Test
    public void testEquals() {
        ComboLeg comboLeg3 = new ComboLeg(1234, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg4 = new ComboLeg(1234, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg5 = new ComboLeg(5678, 2, "SELL", "NASDAQ", 2, 3, "Location");
        
        assertTrue(comboLeg3.equals(comboLeg4));
        assertFalse(comboLeg3.equals(comboLeg5));
    }
}