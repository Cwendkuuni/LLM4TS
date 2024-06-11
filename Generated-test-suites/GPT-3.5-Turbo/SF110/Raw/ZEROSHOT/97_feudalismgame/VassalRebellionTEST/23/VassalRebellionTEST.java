package src;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {

        VassalRebellion rebellion = new VassalRebellion();
        ArrayList vassals = new ArrayList();
        vassals.add("Vassal1");
        vassals.add("Vassal2");
        Map.map.put("Fiefdom1", new Fiefdoms());

        rebellion.perform(vassals);

        // Test cases to validate the behavior of perform() method

        // Test case 1: Loyal vassals are present and enough loyal vassals to cause rebellion
        assertEquals(2, Map.map.get("Fiefdom1").getRebelliousVassals().size());
        assertFalse(Map.map.get("Fiefdom1").isKingdom());

        // Test case 2: Loyal vassals are present but not enough to cause rebellion
        vassals.clear();
        vassals.add("Vassal3");
        Map.map.put("Fiefdom2", new Fiefdoms());
        rebellion.perform(vassals);

        assertEquals(1, Map.map.get("Fiefdom2").getRebelliousVassals().size());
        assertTrue(Map.map.get("Fiefdom2").isKingdom());

        // Test case 3: No loyal vassals present
        vassals.clear();
        Map.map.put("Fiefdom3", new Fiefdoms());
        rebellion.perform(vassals);

        assertEquals(1, Map.map.get("Fiefdom3").getRebelliousVassals().size());
        assertTrue(Map.map.get("Fiefdom3").isKingdom());

        // Test case 4: Loyal vassals present but not enough to cause rebellion based on personal and fiefdom castles
        vassals.clear();
        vassals.add("Vassal4");
        Map.map.put("Fiefdom4", new Fiefdoms());
        Map.map.get("Fiefdom4").setPersonalCastles(1);
        Map.map.get("Fiefdom4").setFiefdomCastles(1);
        rebellion.perform(vassals);

        assertEquals(1, Map.map.get("Fiefdom4").getRebelliousVassals().size());
        assertTrue(Map.map.get("Fiefdom4").isKingdom());

        // Test case 5: Loyal vassals present and enough to cause rebellion based on personal and fiefdom castles
        vassals.clear();
        vassals.add("Vassal5");
        Map.map.put("Fiefdom5", new Fiefdoms());
        Map.map.get("Fiefdom5").setPersonalCastles(10);
        Map.map.get("Fiefdom5").setFiefdomCastles(10);
        rebellion.perform(vassals);

        assertEquals(0, Map.map.get("Fiefdom5").getRebelliousVassals().size());
        assertFalse(Map.map.get("Fiefdom5").isKingdom());
    }
}