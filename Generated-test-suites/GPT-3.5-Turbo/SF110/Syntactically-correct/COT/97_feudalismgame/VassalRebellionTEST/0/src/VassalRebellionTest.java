import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    // Test case for perform() method
    @Test
    public void testPerform() {
        // Create necessary objects and initialize variables
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        VassalRebellion rebellion = new VassalRebellion();
        
        // Mock the Map object
        Map.map.put("Fiefdom1", new Fiefdoms("Fiefdom1"));
        Map.map.get("Fiefdom1").setLoyalVassals(new ArrayList<>());
        Map.map.get("Fiefdom1").setPersonalCastles(1);
        Map.map.get("Fiefdom1").setFiefdomCastles(2);
        
        // Call the perform method
        rebellion.perform(col);
        
        // Assert the expected values after the method execution
        assertEquals("Rebellious vassals should be set to 0", 0, Map.map.get("Fiefdom1").getRebelliousVassals());
        assertFalse("Kingdom should be set to false", Map.map.get("Fiefdom1").isKingdom());
    }
    
}