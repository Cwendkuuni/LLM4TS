import org.junit.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_withValidInputs_shouldSetRebelliousVassals() {
        // create a fiefdom object with loyal vassals and valid castles
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("Fiefdom1");
        fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2")));
        fiefdomObj.setValue(2);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        Map.map.put("Fiefdom1", fiefdomObj);
        
        // create a collection with fiefdom name
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        // call the perform method
        Actions actions = new VassalRebellion();
        actions.perform(col);
        
        // assert that the fiefdom has rebellious vassals and kingdom flag is set to false
        Assert.assertEquals(true, fiefdomObj.isRebelliousVassals());
        Assert.assertEquals(false, fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_withNoLoyalVassals_shouldCreateRollEvent() {
        // create a fiefdom object with no loyal vassals
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("Fiefdom2");
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        Map.map.put("Fiefdom2", fiefdomObj);
        
        // create a collection with fiefdom name
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom2");
        
        // call the perform method
        Actions actions = new VassalRebellion();
        actions.perform(col);
        
        // assert that a RollEvent object is created with the correct fiefdom name
        // ... assert implementation for RollEvent constructor and attributes if available
        
    }
    
    @Test
    public void testPerform_withRebelliousVassalsSizeGreaterThanLoyalVassalsSize_shouldSetRebelliousVassals() {
        // create a fiefdom object with loyal vassals and valid castles
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("Fiefdom3");
        fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2")));
        fiefdomObj.setValue(3);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        Map.map.put("Fiefdom3", fiefdomObj);
        
        // create a collection with fiefdom name
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom3");
        
        // call the perform method
        Actions actions = new VassalRebellion();
        actions.perform(col);
        
        // assert that the fiefdom has rebellious vassals
        Assert.assertEquals(true, fiefdomObj.isRebelliousVassals());
    }
    
    // additional test cases for other methods in the class
    
}