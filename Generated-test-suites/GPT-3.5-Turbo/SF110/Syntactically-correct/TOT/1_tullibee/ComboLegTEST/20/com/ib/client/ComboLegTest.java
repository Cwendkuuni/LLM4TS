import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        assertEquals(null, comboLeg.m_action);
        assertEquals(null, comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor1() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "NYSE", ComboLeg.SAME);
        assertEquals(1, comboLeg1.m_conId);
        assertEquals(2, comboLeg1.m_ratio);
        assertEquals("Buy", comboLeg1.m_action);
        assertEquals("NYSE", comboLeg1.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertEquals(null, comboLeg1.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor2() {
        ComboLeg comboLeg2 = new ComboLeg(2, 3, "Sell", "NASDAQ", ComboLeg.OPEN, 1, "Location1");
        assertEquals(2, comboLeg2.m_conId);
        assertEquals(3, comboLeg2.m_ratio);
        assertEquals("Sell", comboLeg2.m_action);
        assertEquals("NASDAQ", comboLeg2.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);
        assertEquals(1, comboLeg2.m_shortSaleSlot);
        assertEquals("Location1", comboLeg2.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg3 = new ComboLeg(3, 4, "Buy", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg4 = new ComboLeg(3, 4, "Buy", "NYSE", ComboLeg.CLOSE);
        ComboLeg comboLeg5 = new ComboLeg(5, 6, "Sell", "NASDAQ", ComboLeg.UNKNOWN, 2, "Location2");

        assertEquals(comboLeg3, comboLeg4);
        assertNotEquals(comboLeg3, comboLeg5);
        assertNotEquals(comboLeg4, comboLeg5);
    }

}