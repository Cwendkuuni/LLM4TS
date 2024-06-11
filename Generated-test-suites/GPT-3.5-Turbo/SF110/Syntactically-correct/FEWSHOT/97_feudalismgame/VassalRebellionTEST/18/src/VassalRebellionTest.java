import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

public class VassalRebellionTest {
    @Test
    public void testPerformHasLoyalVassals() {
        // Test with loyal vassals
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");

        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("Fiefdom1", fiefdomObj);
        
        vassalRebellion.perform(col);

        // Assert expected values
        assertEquals(2, fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerformNoLoyalVassals() {
        // Test with no loyal vassals
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");

        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        List<String> loyalVassals = new ArrayList<>();
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("Fiefdom1", fiefdomObj);
        
        vassalRebellion.perform(col);

        // Assert expected values
        assertEquals(0, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerformNoPersonalCastles() {
        // Test with no personal castles but with fiefdom castles and loyal vassals
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");

        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(3);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        fiefdomObj.setLoyalVassals(loyalVassals);
        map.put("Fiefdom1", fiefdomObj);
        
        vassalRebellion.perform(col);

        // Assert expected values
        assertEquals(2, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());
    }
}