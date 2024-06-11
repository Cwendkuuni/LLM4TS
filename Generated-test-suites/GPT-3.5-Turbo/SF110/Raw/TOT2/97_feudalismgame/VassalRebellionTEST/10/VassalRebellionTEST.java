import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void perform_basicTest() {
        // Initialize necessary objects
        VassalRebellion rebellion = new VassalRebellion();
        Fiefdoms fiefdom = new Fiefdoms();
        Map.map.put(fiefdom.getName(), fiefdom);
        
        // Create a collection with fiefdom's name
        List<String> col = new ArrayList<>();
        col.add(fiefdom.getName());
        
        // Perform the action
        rebellion.perform(col);
        
        // Assert the changes made by the perform method
        assertEquals(true, fiefdom.isRebelliousVassals());
        assertEquals(false, fiefdom.isKingdom());
    }
    
    @Test
    public void perform_noLoyalVassalsTest() {
        // Initialize necessary objects
        VassalRebellion rebellion = new VassalRebellion();
        Fiefdoms fiefdom = new Fiefdoms();
        Map.map.put(fiefdom.getName(), fiefdom);
        
        // Create a collection with fiefdom's name
        List<String> col = new ArrayList<>();
        col.add(fiefdom.getName());
        
        // Set loyal vassals size to 0
        fiefdom.setLoyalVassals(new ArrayList<>());
        
        // Perform the action
        rebellion.perform(col);
        
        // Assert that RollEvent is created in case of no loyal vassals
        // This is an exception handling scenario
        assertTrue(/* Assert the creation of RollEvent */);
    }
    
    // Add more test cases for other methods as needed
    
}