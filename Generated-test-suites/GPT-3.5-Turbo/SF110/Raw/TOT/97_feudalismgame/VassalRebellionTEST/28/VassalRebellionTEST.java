import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_typicalCase() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        
        // Mocking the necessary objects and their behavior
        Map.map.put("fiefdomName", fiefdomObj);
        fiefdomObj.setName("fiefdomName");
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(5);
        
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");
        
        vassalRebellion.perform(col);
        
        // Assert the expected outcome after performing the method
        assertTrue(fiefdomObj.isKingdom());
        assertTrue(fiefdomObj.isRebelliousVassals(2, true));
        assertFalse(fiefdomObj.isRebelliousVassals(3, true));
    }

    @Test
    public void testPerform_noLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        
        // Mocking the necessary objects and their behavior
        Map.map.put("fiefdomName", fiefdomObj);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");
        
        vassalRebellion.perform(col);
        
        // Assert the expected outcome after performing the method
        assertNull(Map.map.get("fiefdomName"));
    }

    @Test
    public void testPerform_rebelliousVassalsGreaterThanLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        
        // Mocking the necessary objects and their behavior
        Map.map.put("fiefdomName", fiefdomObj);
        fiefdomObj.setName("fiefdomName");
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(10);
        
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");
        
        vassalRebellion.perform(col);
        
        // Assert the expected outcome after performing the method
        assertTrue(fiefdomObj.isRebelliousVassals(10, true));
        assertFalse(fiefdomObj.isRebelliousVassals(10, false));
    }

    @Test
    public void testPerform_negativeRebelliousVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        
        // Mocking the necessary objects and their behavior
        Map.map.put("fiefdomName", fiefdomObj);
        fiefdomObj.setName("fiefdomName");
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(5);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(0);
        
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");
        
        vassalRebellion.perform(col);
        
        // Assert the expected outcome after performing the method
        assertTrue(fiefdomObj.isRebelliousVassals(1, true));
        assertFalse(fiefdomObj.isRebelliousVassals(0, true));
    }
}