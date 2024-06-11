import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1, 0, "location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1, 0, "location1");
        ComboLeg comboLeg3 = new ComboLeg(2, 3, "action2", "exchange2", 2, 1, "location2");

        // Same objects
        assertTrue(comboLeg1.equals(comboLeg1));

        // Different objects with same values
        assertTrue(comboLeg1.equals(comboLeg2));
        assertTrue(comboLeg2.equals(comboLeg1));

        // Different objects with different values
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg3.equals(comboLeg1));
    }

}