import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLeg() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
    }

    @Test
    public void testComboLegIntIntStringStringInt() {
        ComboLeg comboLeg = new ComboLeg(0, 0, null, null, 0);
        assertNotNull(comboLeg);
    }
    
    @Test
    public void testComboLegIntIntStringStringIntIntString() {
        ComboLeg comboLeg = new ComboLeg(0, 0, null, null, 0, 0, null);
        assertNotNull(comboLeg);
    }
    
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "action", "exchange", 1, 0, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "action", "exchange", 1, 0, "location");
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "action", "exchange", 1, 0, "location");
        
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(null));
        assertFalse(comboLeg1.equals(new Object()));
    }
}