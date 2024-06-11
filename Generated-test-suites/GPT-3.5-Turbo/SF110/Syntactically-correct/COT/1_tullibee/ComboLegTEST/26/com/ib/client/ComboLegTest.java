import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "buy", "NYSE", 0, 0, "USA");
        ComboLeg leg2 = new ComboLeg(1, 2, "buy", "NYSE", 0, 0, "USA");
        ComboLeg leg3 = new ComboLeg(2, 2, "sell", "NASDAQ", 1, 1, "Canada");

        // Check if two ComboLeg objects with same values are equal
        assertTrue(leg1.equals(leg2));

        // Check if two ComboLeg objects with different values are not equal
        assertFalse(leg1.equals(leg3));
    }

    @Test
    public void testConstructor() {
        ComboLeg leg = new ComboLeg(1, 2, "buy", "NYSE", 0, 1, "USA");

        // Check if the combo leg object is initialized with correct values
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("buy", leg.m_action);
        assertEquals("NYSE", leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(1, leg.m_shortSaleSlot);
        assertEquals("USA", leg.m_designatedLocation);
    }
}