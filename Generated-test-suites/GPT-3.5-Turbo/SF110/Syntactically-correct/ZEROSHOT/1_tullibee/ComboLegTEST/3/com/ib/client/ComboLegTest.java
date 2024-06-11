import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
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
    public void testParameterizedConstructor1() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", 3);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action", comboLeg.m_action);
        assertEquals("Exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testParameterizedConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", 3, 4, "Location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action", comboLeg.m_action);
        assertEquals("Exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(4, comboLeg.m_shortSaleSlot);
        assertEquals("Location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Buy", "NYSE", ComboLeg.SAME, 0, null);
        ComboLeg leg2 = new ComboLeg(1, 1, "Buy", "NYSE", ComboLeg.SAME, 0, null);
        ComboLeg leg3 = new ComboLeg(2, 1, "Buy", "NYSE", ComboLeg.SAME, 0, null);
        
        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
    }

}