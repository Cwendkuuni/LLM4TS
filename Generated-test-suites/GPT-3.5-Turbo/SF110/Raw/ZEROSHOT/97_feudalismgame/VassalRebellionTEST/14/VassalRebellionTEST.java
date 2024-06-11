import org.junit.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion rebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        Map.map.put("TestFiefdom", fiefdomObj);
        
        // Test case 1: There are loyal vassals and enough rebellious vassals
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setRebelliousVassals(5, false);
        fiefdomObj.setValue(10);
        rebellion.perform(Map.map.keySet());
        assertTrue(fiefdomObj.isKingdom());
        
        // Test case 2: There are loyal vassals and not enough rebellious vassals
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setRebelliousVassals(5, false);
        fiefdomObj.setValue(2);
        rebellion.perform(Map.map.keySet());
        assertFalse(fiefdomObj.isKingdom());
        
        // Test case 3: There are no loyal vassals
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        rebellion.perform(Map.map.keySet());
        assertNotNull(fiefdomObj.getRollEvent());
    }
}