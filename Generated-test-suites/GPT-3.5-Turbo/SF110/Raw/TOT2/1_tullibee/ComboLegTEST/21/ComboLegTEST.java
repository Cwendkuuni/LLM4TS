
import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {
    
    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
    }
    
    @Test
    public void testComboLegParameterizedConstructor1() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 0);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
    }
    
    @Test
    public void testComboLegParameterizedConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 0, 3, "location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("location", comboLeg.m_designatedLocation);
    }
    
    @Test
    public void testEqualsMethod() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 0);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 0);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
}
