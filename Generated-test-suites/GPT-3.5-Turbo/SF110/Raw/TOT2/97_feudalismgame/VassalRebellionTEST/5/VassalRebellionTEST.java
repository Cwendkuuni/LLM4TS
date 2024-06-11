package src;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", new Fiefdoms("TestFiefdom", 5, 10, new ArrayList<>()));
        
        Collection<String> testCollection = new ArrayList<>();
        testCollection.add("TestFiefdom");
        
        vassalRebellion.perform(testCollection);
        
        // Test if the fiefdomObj is properly initialized
        assertNotNull(vassalRebellion.fiefdomObj);
        
        // Test for the condition when there are loyal vassals
        Map.map.get("TestFiefdom").getLoyalVassals().add("LoyalVassal");
        vassalRebellion.perform(testCollection);
        
        // Test for the case when fiefdomObj has more loyal vassals than its value
        Map.map.get("TestFiefdom").setLoyalVassalsCount(10); // Setting loyal vassals count more than the value of fiefdom
        vassalRebellion.perform(testCollection);
        
        // Test for the case when personalCastles and fiefdomCastles are greater than 0
        Map.map.get("TestFiefdom").setPersonalCastles(2);
        Map.map.get("TestFiefdom").setFiefdomCastles(3);
        vassalRebellion.perform(testCollection);
        
        // Test for the case when there are no loyal vassals
        Map.map.get("TestFiefdom").getLoyalVassals().clear();
        vassalRebellion.perform(testCollection);
        
        // Test for exception handling when the fiefdomObj is null
        vassalRebellion.fiefdomObj = null;
        assertThrows(NullPointerException.class, () -> vassalRebellion.perform(testCollection));
    }
}