
import org.junit.Test;

public class VassalRebellionTest {

    @Test
    public void testPerform_noRebelliousVassals() {
        // Create test input
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom 1");
        vassalRebellion.setMap(Map.mapInstance());
        Map.map.put("Fiefdom 1", new Fiefdoms());
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assert the results
        assertEquals(0, Map.map.get("Fiefdom 1").getRebelliousVassals());
        assertTrue(Map.map.get("Fiefdom 1").isKingdom());
    }
    
    @Test
    public void testPerform_rebelliousVassalsAndNoCastles() {
        // Create test input
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom 2");
        vassalRebellion.setMap(Map.mapInstance());
        Fiefdoms fiefdoms = new Fiefdoms();
        fiefdoms.setLoyalVassals(Arrays.asList("Vassal 1", "Vassal 2", "Vassal 3"));
        Map.map.put("Fiefdom 2", fiefdoms);
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assert the results
        assertEquals(3, Map.map.get("Fiefdom 2").getRebelliousVassals());
        assertFalse(Map.map.get("Fiefdom 2").isKingdom());
    }
    
    @Test
    public void testPerform_rebelliousVassalsAndCastles() {
        // Create test input
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom 3");
        vassalRebellion.setMap(Map.mapInstance());
        Fiefdoms fiefdoms = new Fiefdoms();
        fiefdoms.setLoyalVassals(Arrays.asList("Vassal 1", "Vassal 2", "Vassal 3", "Vassal 4", "Vassal 5"));
        fiefdoms.setPersonalCastles(1);
        fiefdoms.setFiefdomCastles(2);
        Map.map.put("Fiefdom 3", fiefdoms);
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assert the results
        assertEquals(0, Map.map.get("Fiefdom 3").getRebelliousVassals());
        assertTrue(Map.map.get("Fiefdom 3").isKingdom());
    }
    
    @Test
    public void testPerform_noLoyalVassals() {
        // Create test input
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom 4");
        vassalRebellion.setMap(Map.mapInstance());
        Map.map.put("Fiefdom 4", new Fiefdoms());
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assert the results
        assertEquals(0, Map.map.get("Fiefdom 4").getRebelliousVassals());
        assertTrue(Map.map.get("Fiefdom 4").isKingdom());
        // Verify RollEvent constructor was called
    }
}
