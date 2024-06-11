package src;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case 1: Loyal vassals exist and are sufficient to trigger rebellion
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("fiefdomName", new Fiefdoms());
        Map.map.get("fiefdomName").setLoyalVassals(new ArrayList<>());
        Map.map.get("fiefdomName").setPersonalCastles(0);
        Map.map.get("fiefdomName").setFiefdomCastles(0);
        vassalRebellion.perform(Collections.singleton("fiefdomName"));
        assertTrue(Map.map.get("fiefdomName").isRebelliousVassals());
        assertFalse(Map.map.get("fiefdomName").isKingdom());

        // Test case 2: Loyal vassals exist but are insufficient to trigger rebellion
        VassalRebellion vassalRebellion2 = new VassalRebellion();
        Map.map.put("fiefdomName2", new Fiefdoms());
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("vassal1");
        Map.map.get("fiefdomName2").setLoyalVassals(loyalVassals);
        Map.map.get("fiefdomName2").setPersonalCastles(0);
        Map.map.get("fiefdomName2").setFiefdomCastles(0);
        Map.map.get("fiefdomName2").setValue(100);
        vassalRebellion2.perform(Collections.singleton("fiefdomName2"));
        assertFalse(Map.map.get("fiefdomName2").isRebelliousVassals());

        // Test case 3: Loyal vassals and personal/fiefdom castles exist, but rebellion value is not reached
        VassalRebellion vassalRebellion3 = new VassalRebellion();
        Map.map.put("fiefdomName3", new Fiefdoms());
        List<String> loyalVassals3 = new ArrayList<>();
        loyalVassals3.add("vassal1");
        Map.map.get("fiefdomName3").setLoyalVassals(loyalVassals3);
        Map.map.get("fiefdomName3").setPersonalCastles(1);
        Map.map.get("fiefdomName3").setFiefdomCastles(2);
        Map.map.get("fiefdomName3").setValue(100);
        vassalRebellion3.perform(Collections.singleton("fiefdomName3"));
        assertFalse(Map.map.get("fiefdomName3").isRebelliousVassals());

        // Test case 4: No loyal vassals
        VassalRebellion vassalRebellion4 = new VassalRebellion();
        Map.map.put("fiefdomName4", new Fiefdoms());
        vassalRebellion4.perform(Collections.singleton("fiefdomName4"));
        assertFalse(Map.map.get("fiefdomName4").isRebelliousVassals());
        assertNotNull(RollEvent.lastEvent);
    }
}