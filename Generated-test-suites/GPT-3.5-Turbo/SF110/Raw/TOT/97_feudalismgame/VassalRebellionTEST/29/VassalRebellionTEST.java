
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    
    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }
    
    // Test case for perform method when there are loyal vassals and rebel count is equal to value
    @Test
    public void testPerform_withLoyalVassals_rebelCountEqualToValue() {
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom");
        fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal1", "Vassal2")));
        map.put("TestFiefdom", fiefdomObj);
        Map.map = map;
        
        Collection col = Arrays.asList("TestFiefdom");
        
        vassalRebellion.perform(col);
        
        assertEquals(true, fiefdomObj.getRebelliousVassals());
        assertEquals(false, fiefdomObj.getKingdom());
    }
    
    // Test case for perform method when there are loyal vassals and rebel count is less than value
    @Test
    public void testPerform_withLoyalVassals_rebelCountLessThanValue() {
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom");
        fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal1")));
        map.put("TestFiefdom", fiefdomObj);
        Map.map = map;
        
        Collection col = Arrays.asList("TestFiefdom");
        
        vassalRebellion.perform(col);
        
        assertEquals(true, fiefdomObj.getRebelliousVassals());
    }
    
    // Test case for perform method when there are no loyal vassals
    @Test
    public void testPerform_withNoLoyalVassals() {
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom");
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        map.put("TestFiefdom", fiefdomObj);
        Map.map = map;
        
        Collection col = Arrays.asList("TestFiefdom");
        
        vassalRebellion.perform(col);
        
        // Assertions for the expected behavior
    }
    
    // Additional test cases for other methods can be added here
    
}

