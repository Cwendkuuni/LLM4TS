import org.junit.*;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg3 = new ComboLeg(2, 2, "SELL", "NASDAQ", 2);

        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
    }
}