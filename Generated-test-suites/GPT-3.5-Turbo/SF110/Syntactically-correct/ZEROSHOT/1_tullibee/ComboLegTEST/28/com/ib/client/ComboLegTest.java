import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(null, comboLeg.m_action);
        assertEquals(null, comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
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
        assertEquals(null, comboLeg.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "US");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("NYSE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("US", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "US");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 1, "US");
        ComboLeg comboLeg3 = new ComboLeg(2, 3, "SELL", "NASDAQ", 2, 0, null);

        assertEquals(true, comboLeg1.equals(comboLeg1));
        assertEquals(true, comboLeg1.equals(comboLeg2));
        assertEquals(false, comboLeg1.equals(null));
        assertEquals(false, comboLeg1.equals(comboLeg3));
    }
}