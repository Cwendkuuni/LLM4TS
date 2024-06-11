import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import src.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_WithLoyalVassalsAndEnoughRebelliousVassals() {
        // Setup
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("FiefdomA", fiefdomObj);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(10);
        
        // Execution
        vassalRebellion.perform(new ArrayList<>(Collections.singletonList("FiefdomA")));
        
        // Assertion
        assertTrue(fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_WithLoyalVassalsAndNotEnoughRebelliousVassals() {
        // Setup
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("FiefdomA", fiefdomObj);
        List<Vassal> loyalVassals = new ArrayList<>();
        loyalVassals.add(new Vassal());
        loyalVassals.add(new Vassal());
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setValue(2);
        
        // Execution
        vassalRebellion.perform(new ArrayList<>(Collections.singletonList("FiefdomA")));
        
        // Assertion
        assertEquals(2, fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_WithPersonalCastlesAndFiefdomCastlesAndEnoughRebelliousVassals() {
        // Setup
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("FiefdomA", fiefdomObj);
        List<Vassal> loyalVassals = new ArrayList<>();
        loyalVassals.add(new Vassal());
        loyalVassals.add(new Vassal());
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        
        // Execution
        vassalRebellion.perform(new ArrayList<>(Collections.singletonList("FiefdomA")));
        
        // Assertion
        assertEquals(5, fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_WithoutLoyalVassals() {
        // Setup
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("FiefdomA", fiefdomObj);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        
        // Execution
        vassalRebellion.perform(new ArrayList<>(Collections.singletonList("FiefdomA")));
        
        // Assertion
        // verify new RollEvent is triggered
    }
}