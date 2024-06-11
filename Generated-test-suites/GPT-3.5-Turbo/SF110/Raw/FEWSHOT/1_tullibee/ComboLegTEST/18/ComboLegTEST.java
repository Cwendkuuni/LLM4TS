import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ComboLegTest {
  
    private ComboLeg comboLeg;
  
    @Before
    public void setUp() {
        comboLeg = new ComboLeg();
    }
  
    @Test
    public void testDefaultConstructor() {
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
  
    @Test
    public void testParameterizedConstructor1() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1);
      
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
  
    @Test
    public void testParameterizedConstructor2() {
        ComboLeg comboLeg = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 3, "USA");
      
        assertEquals(3, comboLeg.m_conId);
        assertEquals(4, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(2, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("USA", comboLeg.m_designatedLocation);
    }
  
    @Test
    public void testEquals() {
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
      
        assertTrue(comboLeg.equals(comboLeg));
        assertTrue(comboLeg.equals(comboLeg2));
      
        comboLeg2 = new ComboLeg(3, 4, "SELL", "NASDAQ", 2, 3, "USA");
        assertFalse(comboLeg.equals(comboLeg2));
    }
}