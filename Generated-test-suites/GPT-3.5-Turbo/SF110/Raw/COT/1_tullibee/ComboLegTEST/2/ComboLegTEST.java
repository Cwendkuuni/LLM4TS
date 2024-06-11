import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 0);
        assertTrue(leg1.equals(leg2));
    }
}