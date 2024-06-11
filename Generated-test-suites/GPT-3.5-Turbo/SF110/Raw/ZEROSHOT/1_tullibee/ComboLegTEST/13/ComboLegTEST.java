import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "action1", "exchange1", 1, 0, "location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "action1", "exchange1", 1, 0, "location1");
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "action2", "exchange2", 2, 1, "location2");

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}