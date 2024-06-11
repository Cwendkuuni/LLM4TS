
import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(2, 1, "BUY", "NYSE", 1);
        ComboLeg leg4 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg5 = new ComboLeg(1, 1, "SELL", "NYSE", 1);
        ComboLeg leg6 = new ComboLeg(1, 1, "BUY", "NASDAQ", 1);
        ComboLeg leg7 = new ComboLeg(1, 1, "BUY", "NYSE", 2);
        ComboLeg leg8 = new ComboLeg(1, 1, "BUY", "NYSE", 1);

        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
        assertFalse(leg1.equals(leg4));
        assertFalse(leg1.equals(leg5));
        assertFalse(leg1.equals(leg6));
        assertFalse(leg1.equals(leg7));
        assertTrue(leg1.equals(leg8));
    }
}
