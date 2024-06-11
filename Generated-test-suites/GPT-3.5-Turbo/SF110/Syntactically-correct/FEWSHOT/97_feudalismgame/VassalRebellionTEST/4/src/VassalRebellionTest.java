package src;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VassalRebellionTest {
  
    @Test
    public void testPerform_withLoyalVassals() {
        Map.map.put("fiefdomName", new Fiefdoms("fiefdomName"));
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal1");
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal2");
        Map.map.get("fiefdomName").setValue(1);
        Map.map.get("fiefdomName").setPersonalCastles(1);
        Map.map.get("fiefdomName").setFiefdomCastles(1);
        
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("fiefdomName"));
        
        assertEquals(1, Map.map.get("fiefdomName").getRebelliousVassals().size());
        assertTrue(!Map.map.get("fiefdomName").isKingdom());
    }
  
    @Test
    public void testPerform_withoutLoyalVassals() {
        Map.map.put("fiefdomName", new Fiefdoms("fiefdomName"));
        
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("fiefdomName"));
        
        assertTrue(Map.map.get("fiefdomName").getRebelliousVassals().isEmpty());
        assertTrue(Map.map.get("fiefdomName").isKingdom());
    }
  
    @Test
    public void testPerform_withPersonalAndFiefdomCastles() {
        Map.map.put("fiefdomName", new Fiefdoms("fiefdomName"));
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal1");
        Map.map.get("fiefdomName").setValue(1);
        Map.map.get("fiefdomName").setPersonalCastles(1);
        Map.map.get("fiefdomName").setFiefdomCastles(1);
        
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("fiefdomName"));
        
        assertEquals(0, Map.map.get("fiefdomName").getRebelliousVassals().size());
    }
  
    @Test
    public void testPerform_withoutPersonalAndFiefdomCastles() {
        Map.map.put("fiefdomName", new Fiefdoms("fiefdomName"));
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal1");
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal2");
        Map.map.get("fiefdomName").setValue(1);
        
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("fiefdomName"));
        
        assertEquals(1, Map.map.get("fiefdomName").getRebelliousVassals().size());
    }
  
    @Test
    public void testPerform_withMultipleConditions() {
        Map.map.put("fiefdomName", new Fiefdoms("fiefdomName"));
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal1");
        Map.map.get("fiefdomName").getLoyalVassals().add("loyalVassal2");
        Map.map.get("fiefdomName").setValue(2);
        Map.map.get("fiefdomName").setPersonalCastles(1);
        Map.map.get("fiefdomName").setFiefdomCastles(1);
        
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Arrays.asList("fiefdomName"));
        
        assertEquals(1, Map.map.get("fiefdomName").getRebelliousVassals().size());
    }
}