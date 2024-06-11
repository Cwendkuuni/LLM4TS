import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_withLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // setup test data
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        fiefdomObj.setPersonalCastles(5);
        fiefdomObj.setFiefdomCastles(10);
        List<String> loyalVassals = Arrays.asList("Vassal1", "Vassal2", "Vassal3");
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("TestFiefdom", fiefdomObj);
        Map.setMap(map);
        
        // perform the method to be tested
        vassalRebellion.perform(Arrays.asList("TestFiefdom")); 
        
        // assert the expected behavior
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(3, fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_withoutLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // setup test data
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        List<String> loyalVassals = new ArrayList<>();
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("TestFiefdom", fiefdomObj);
        Map.setMap(map);
        
        // perform the method to be tested
        vassalRebellion.perform(Arrays.asList("TestFiefdom")); 
        
        // assert the expected behavior
        assertFalse(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
        assertEquals(0, fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_withValidRebelliousVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // setup test data
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(1);
        List<String> loyalVassals = Arrays.asList("Vassal1", "Vassal2", "Vassal3", "Vassal4", "Vassal5");
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("TestFiefdom", fiefdomObj);
        Map.setMap(map);
        
        // perform the method to be tested
        vassalRebellion.perform(Arrays.asList("TestFiefdom")); 
        
        // assert the expected behavior
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(5, fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_withInvalidRebelliousVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // setup test data
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        List<String> loyalVassals = Arrays.asList("Vassal1", "Vassal2", "Vassal3");
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("TestFiefdom", fiefdomObj);
        Map.setMap(map);
        
        // perform the method to be tested
        vassalRebellion.perform(Arrays.asList("TestFiefdom")); 
        
        // assert the expected behavior
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(3, fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_withNoLoyalVassalsAndRollEvent() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // setup test data
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        List<String> loyalVassals = new ArrayList<>();
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("TestFiefdom", fiefdomObj);
        Map.setMap(map);
        
        // perform the method to be tested
        vassalRebellion.perform(Arrays.asList("TestFiefdom")); 
        
        // assert the expected behavior
        // expect a RollEvent to be created
    }
}