package src;

import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Create objects and initialize variables
        VassalRebellion rebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("Fiefdom1", fiefdomObj);

        // Test with loyal vassals
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        rebellion.perform(Arrays.asList("Fiefdom1"));
        assertEquals(0, fiefdomObj.getRebelliousVassals().size());
        assertEquals(true, fiefdomObj.isKingdom());

        // Test with loyal vassals and enough value
        fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2")));
        fiefdomObj.setValue(2);
        rebellion.perform(Arrays.asList("Fiefdom1"));
        assertEquals(2, fiefdomObj.getRebelliousVassals().size());
        assertEquals(false, fiefdomObj.isKingdom());

        // Test with loyal vassals and not enough value
        fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2")));
        fiefdomObj.setValue(4);
        rebellion.perform(Arrays.asList("Fiefdom1"));
        assertEquals(2, fiefdomObj.getRebelliousVassals().size());
        assertEquals(false, fiefdomObj.isKingdom());

        // Test with personal castles and fiefdom castles
        fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2", "Vassal3")));
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        rebellion.perform(Arrays.asList("Fiefdom1"));
        assertEquals(1, fiefdomObj.getRebelliousVassals().size());
        assertEquals(false, fiefdomObj.isKingdom());
    }
}