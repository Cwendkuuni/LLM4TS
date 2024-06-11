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
    public void testConstructorWithParameters() {
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
    public void testFullConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Buy", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_shortSaleSlot);
        assertEquals("Chicago", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsWithEqualComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        ComboLeg comboLeg2 = new ComboLeg(2, 3, "Sell", "NASDAQ", 0, 0, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Buy", "NYSE", 1, 2, "Chicago");
        assertFalse(comboLeg.equals("some string"));
    }
}