import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testEquals_SameObject_ShouldReturnTrue() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        assertTrue(comboLeg.equals(comboLeg));
    }
    
    @Test
    public void testEquals_NullObject_ShouldReturnFalse() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        assertFalse(comboLeg.equals(null));
    }
    
    @Test
    public void testEquals_DifferentType_ShouldReturnFalse() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        assertFalse(comboLeg.equals("test"));
    }
    
    @Test
    public void testEquals_EqualComboLeg_ShouldReturnTrue() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        assertTrue(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_DifferentConId_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "NYSE", ComboLeg.SAME);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_DifferentRatio_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "NYSE", ComboLeg.SAME);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_DifferentAction_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "NYSE", ComboLeg.SAME);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_DifferentExchange_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.SAME);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_DifferentOpenClose_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.CLOSE);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentShortSaleSlot_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 1, null);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
    
    @Test
    public void testEquals_DifferentDesignatedLocation_ShouldReturnFalse() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NYSE", ComboLeg.SAME, 0, "LOC");
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}