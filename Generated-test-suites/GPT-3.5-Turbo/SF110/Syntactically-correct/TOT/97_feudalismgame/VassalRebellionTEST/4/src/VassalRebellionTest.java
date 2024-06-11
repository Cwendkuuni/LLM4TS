import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_typicalUseCase() {
        // Typical use case where there are loyal vassals and personal/fiefdom castles
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Create a mock collection with one fiefdom name
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        // Create a mock Map with one fiefdom object
        Map<String, Fiefdom> map = new HashMap<>();
        Fiefdom fiefdomObj = new Fiefdom();
        map.put("Fiefdom1", fiefdomObj);
        
        // Set loyal vassals size, personal castles, and fiefdom castles
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(5);
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assertion for expected results
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(0, fiefdomObj.getLoyalVassals().size());
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_edgeCase() {
        // Edge case where there are loyal vassals and no personal/fiefdom castles
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Create a mock collection with one fiefdom name
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        // Create a mock Map with one fiefdom object
        Map<String, Fiefdom> map = new HashMap<>();
        Fiefdom fiefdomObj = new Fiefdom();
        map.put("Fiefdom1", fiefdomObj);
        
        // Set loyal vassals size, personal castles, and fiefdom castles
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setValue(5);
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assertion for expected results
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(2, fiefdomObj.getLoyalVassals().size());
    }
    
    @Test
    public void testPerform_errorScenario() {
        // Error scenario where there are no loyal vassals
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Create a mock collection with one fiefdom name
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        // Create a mock Map with one fiefdom object
        Map<String, Fiefdom> map = new HashMap<>();
        Fiefdom fiefdomObj = new Fiefdom();
        map.put("Fiefdom1", fiefdomObj);
        
        // Perform the action
        vassalRebellion.perform(col);
        
        // Assertion for expected results
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertNotNull(fiefdomObj.getName());
    }
}