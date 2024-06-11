import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    // Test cases for edge cases and exceptions
    
    @Test
    public void testEquals_nullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEquals_sameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEquals_differentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(2, 1, "Buy", "NYSE", 1);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "NYSE", 1);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "NYSE", 2);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "NYSE", 1, 1, null);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Sell", "NYSE", 1, 1, null);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "NASDAQ", 1, 1, null);
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEquals_differentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "NYSE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "NYSE", 1, 1, "A");
        
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}