
import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testEquals_sameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEquals_nullObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        assertFalse(comboLeg1.equals(null));
    }
    
    @Test
    public void testEquals_differentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(2, 1, "SELL", "NASDAQ", 2);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_equalObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
}
