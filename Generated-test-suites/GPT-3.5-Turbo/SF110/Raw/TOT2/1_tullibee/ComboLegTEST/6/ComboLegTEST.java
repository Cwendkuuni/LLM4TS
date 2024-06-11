import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testComboLegConstructorWithNoArgs() {
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
    public void testComboLegConstructorWithPartialArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(ComboLeg.CLOSE, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorWithAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "SELL", "NASDAQ", ComboLeg.OPEN, 1, "USA");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("SELL", comboLeg.m_action);
        assertEquals("NASDAQ", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("USA", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsMethod() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEqualsMethodWithDifferentComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg2 = new ComboLeg(3, 4, "SELL", "NASDAQ", ComboLeg.OPEN, 1, "USA");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test(expected = ClassCastException.class)
    public void testEqualsMethodWithDifferentClass() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        comboLeg.equals(new Object());
    }

}