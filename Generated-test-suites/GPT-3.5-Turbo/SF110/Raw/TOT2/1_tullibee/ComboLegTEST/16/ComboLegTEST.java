import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testParameterizedConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Sell", "NYSE", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

}