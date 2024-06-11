import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals_sameObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEquals_nullObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        ComboLeg comboLeg = new ComboLeg();
        Object obj = new Object();
        assertFalse(comboLeg.equals(obj));
    }
    
    @Test
    public void testEquals_equalComboLeg() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 1);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_conId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "action", "exchange", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_ratio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "action", "exchange", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_openClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 2);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_action() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action2", "exchange", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_exchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange1", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange2", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_designatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1, 0, "location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 1, 0, "location2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_notEqualComboLeg_shortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 1, 1, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}