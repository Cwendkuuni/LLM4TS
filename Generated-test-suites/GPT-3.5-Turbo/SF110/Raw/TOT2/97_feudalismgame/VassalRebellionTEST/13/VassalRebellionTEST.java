import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_WithLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("Fiefdom1", new Fiefdoms("Fiefdom1"));
        Map.map.get("Fiefdom1").setLoyalVassals(new ArrayList<>());
        Map.map.get("Fiefdom1").setValue(5);
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        vassalRebellion.perform(col);
        
        assertTrue(Map.map.get("Fiefdom1").isRebelliousVassals());
        assertFalse(Map.map.get("Fiefdom1").isKingdom());
    }
    
    @Test
    public void testPerform_WithNoLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("Fiefdom2", new Fiefdoms("Fiefdom2"));
        Map.map.get("Fiefdom2").setLoyalVassals(new ArrayList<>());
        Map.map.get("Fiefdom2").setValue(5);
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom2");
        
        vassalRebellion.perform(col);
        
        // Check if RollEvent is created
    }
    
    @Test
    public void testPerform_WithPersonalAndFiefdomCastles() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("Fiefdom3", new Fiefdoms("Fiefdom3"));
        Map.map.get("Fiefdom3").setLoyalVassals(new ArrayList<>());
        Map.map.get("Fiefdom3").setPersonalCastles(3);
        Map.map.get("Fiefdom3").setFiefdomCastles(2);
        Map.map.get("Fiefdom3").setValue(5);
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom3");
        
        vassalRebellion.perform(col);
        
        assertTrue(Map.map.get("Fiefdom3").isRebelliousVassals());
    }
}