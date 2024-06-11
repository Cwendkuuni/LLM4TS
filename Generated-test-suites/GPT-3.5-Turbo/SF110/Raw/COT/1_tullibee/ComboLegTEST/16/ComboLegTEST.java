import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 1, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg2 = new ComboLeg(1, 1, "BUY", "NYSE", ComboLeg.OPEN);
        ComboLeg leg3 = new ComboLeg(2, 1, "BUY", "NYSE", ComboLeg.OPEN);

        // Test that leg1 is equal to leg2
        assertTrue(leg1.equals(leg2));
        // Test that leg1 is not equal to leg3
        assertFalse(leg1.equals(leg3));
    }
}