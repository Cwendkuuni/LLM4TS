import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    
    @Test
    public void testEquals_sameObject_shouldReturnTrue() {
        ComboLeg leg = new ComboLeg();
        assertTrue(leg.equals(leg));
    }
    
    @Test
    public void testEquals_nullObject_shouldReturnFalse() {
        ComboLeg leg = new ComboLeg();
        assertFalse(leg.equals(null));
    }
    
    @Test
    public void testEquals_differentClass_shouldReturnFalse() {
        ComboLeg leg = new ComboLeg();
        assertFalse(leg.equals("Not a ComboLeg object"));
    }
    
    @Test
    public void testEquals_equalComboLegs_shouldReturnTrue() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        assertTrue(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_unequalComboLegs_shouldReturnFalse() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(2, 3, "SELL", "NASDAQ", 2);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_nullAction_shouldReturnFalse() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, null, "NYSE", 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_nullExchange_shouldReturnFalse() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", null, 1);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_nullDesignatedLocation_shouldReturnFalse() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, null);
        assertFalse(leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_unequalAttributes_shouldReturnFalse() {
        ComboLeg leg1 = new ComboLeg(1, 2, "BUY", "NYSE", 1, 0, "LOC1");
        ComboLeg leg2 = new ComboLeg(2, 3, "SELL", "NASDAQ", 2, 1, "LOC2");
        assertFalse(leg1.equals(leg2));
    }
    
}

