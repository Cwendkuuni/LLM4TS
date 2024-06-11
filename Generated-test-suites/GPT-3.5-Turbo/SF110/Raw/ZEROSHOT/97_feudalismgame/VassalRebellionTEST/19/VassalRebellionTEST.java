import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Create VassalRebellion object
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Create dummy data for testing
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("TestFiefdom", fiefdomObj);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        loyalVassals.add("Vassal3");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        fiefdomObj.setValue(5);
        
        // Test when loyal vassals exist
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(2, fiefdomObj.getRebelliousVassals());
        
        // Test when personal castles and fiefdom castles are greater than 0
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(2);
        vassalRebellion.perform(col);
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(1, fiefdomObj.getRebelliousVassals());
        
        // Test when no loyal vassals exist
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        vassalRebellion.perform(col);
        // Assert that RollEvent is created (unable to test actual RollEvent)
    }
}