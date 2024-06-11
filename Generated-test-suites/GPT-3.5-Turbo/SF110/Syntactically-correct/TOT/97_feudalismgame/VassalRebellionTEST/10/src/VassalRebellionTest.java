import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_withLoyalVassals() {
        // Create a fiefdom with loyal vassals
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2", "Vassal3"));
        fiefdomObj.setValue(2);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        Map.map.put("Fiefdom1", fiefdomObj);
        
        // Perform the action
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("Fiefdom1"));
        
        // Assert that rebellious vassals are set correctly
        assertTrue(fiefdomObj.getRebelliousVassals());
        
        // Assert that the fiefdom is no longer a kingdom
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_withLoyalVassalsButNotEnoughRebelliousVassals() {
        // Create a fiefdom with loyal vassals
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2", "Vassal3"));
        fiefdomObj.setValue(5);
        Map.map.put("Fiefdom1", fiefdomObj);
        
        // Perform the action
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("Fiefdom1"));
        
        // Assert that rebellious vassals are set correctly
        assertEquals(fiefdomObj.getRebelliousVassals(), fiefdomObj.getLoyalVassals().size());
    }
    
    @Test
    public void testPerform_withoutLoyalVassals() {
        // Create a fiefdom without loyal vassals
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("Fiefdom1", fiefdomObj);
        
        // Perform the action
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("Fiefdom1"));
        
        // Assert that a RollEvent is created
        // TODO: Assert the creation of a RollEvent
    }
}