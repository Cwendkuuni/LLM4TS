import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NASDAQ";
        int openClose = 1;
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }

    @Test
    public void testParameterizedConstructor2() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "NASDAQ";
        int openClose = 1;
        int shortSaleSlot = 3;
        String designatedLocation = "NY";
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 3, "NY");
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 3, "NY");
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsSameValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 3, "NY");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 3, "NY");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NASDAQ", 1, 3, "NY");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NASDAQ", 1, 3, "NY");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}