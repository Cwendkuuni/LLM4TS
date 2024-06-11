import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals_sameObject() {
        ComboLeg leg = new ComboLeg();
        assertTrue(leg.equals(leg));
    }

    @Test
    public void testEquals_nullObject() {
        ComboLeg leg1 = new ComboLeg();
        assertFalse(leg1.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        ComboLeg leg = new ComboLeg();
        assertFalse(leg.equals("String"));
    }

    @Test
    public void testEquals_equalLegs() {
        ComboLeg leg1 = new ComboLeg(1, 2, "buy", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "nyse", 1);
        assertTrue(leg1.equals(leg2));
    }

    @Test
    public void testEquals_differentLegs() {
        ComboLeg leg1 = new ComboLeg(1, 2, "buy", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 3, "sell", "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
}