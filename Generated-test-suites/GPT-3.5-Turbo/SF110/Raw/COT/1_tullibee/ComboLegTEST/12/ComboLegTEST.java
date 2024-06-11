import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg();
        ComboLeg leg2 = new ComboLeg();

        assertEquals(leg1, leg2);

        leg1.m_conId = 1;
        assertNotEquals(leg1, leg2);

        leg2.m_conId = 1;

        assertEquals(leg1, leg2);

        leg1.m_ratio = 2;
        assertNotEquals(leg1, leg2);

        leg2.m_ratio = 2;

        assertEquals(leg1, leg2);

        leg1.m_action = "BUY";
        assertNotEquals(leg1, leg2);

        leg2.m_action = "BUY";

        assertEquals(leg1, leg2);

        leg1.m_exchange = "NYSE";
        assertNotEquals(leg1, leg2);

        leg2.m_exchange = "NYSE";

        assertEquals(leg1, leg2);

        leg1.m_openClose = ComboLeg.CLOSE;
        assertNotEquals(leg1, leg2);

        leg2.m_openClose = ComboLeg.CLOSE;

        assertEquals(leg1, leg2);

        leg1.m_shortSaleSlot = 1;
        assertNotEquals(leg1, leg2);

        leg2.m_shortSaleSlot = 1;

        assertEquals(leg1, leg2);

        leg1.m_designatedLocation = "LOC";
        assertNotEquals(leg1, leg2);

        leg2.m_designatedLocation = "LOC";

        assertEquals(leg1, leg2);
    }
}