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
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testFullConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_shortSaleSlot);
        assertEquals("location", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        assertFalse(comboLeg1.equals(null));
    }

    @Test
    public void testEqualsDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        ComboLeg comboLeg2 = new ComboLeg(2, 3, "action2", "exchange2", 2, 3, "location2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsSameValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1, 2, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action2", "exchange2", 2, 3, "location2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}