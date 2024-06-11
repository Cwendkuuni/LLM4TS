package src;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case 1: fiefdomObj.getLoyalVassals().size() > 0 and fiefdomObj.getLoyalVassals().size() - fiefdomObj.getValue() >= 0
        VassalRebellion vassalRebellion1 = new VassalRebellion();
        Fiefdoms fiefdomObj1 = new Fiefdoms();
        fiefdomObj1.setPersonalCastles(5);
        fiefdomObj1.setFiefdomCastles(2);
        fiefdomObj1.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal1", "Vassal2", "Vassal3")));
        Map.map.put(fiefdomObj1.getName(), fiefdomObj1);
        Collection<String> col1 = new ArrayList<String>(Arrays.asList(fiefdomObj1.getName()));
        vassalRebellion1.perform(col1);
        assertTrue(fiefdomObj1.isRebelliousVassals());
        assertFalse(fiefdomObj1.isKingdom());

        // Test case 2: fiefdomObj.getLoyalVassals().size() > 0 and fiefdomObj.getLoyalVassals().size() - fiefdomObj.getValue() < 0
        VassalRebellion vassalRebellion2 = new VassalRebellion();
        Fiefdoms fiefdomObj2 = new Fiefdoms();
        fiefdomObj2.setPersonalCastles(3);
        fiefdomObj2.setFiefdomCastles(1);
        fiefdomObj2.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal1", "Vassal2", "Vassal3")));
        Map.map.put(fiefdomObj2.getName(), fiefdomObj2);
        Collection<String> col2 = new ArrayList<String>(Arrays.asList(fiefdomObj2.getName()));
        vassalRebellion2.perform(col2);
        assertTrue(fiefdomObj2.isRebelliousVassals());
        assertTrue(fiefdomObj2.isKingdom());

        // Test case 3: fiefdomObj.getLoyalVassals().size() = 0
        VassalRebellion vassalRebellion3 = new VassalRebellion();
        Fiefdoms fiefdomObj3 = new Fiefdoms();
        fiefdomObj3.setPersonalCastles(2);
        fiefdomObj3.setFiefdomCastles(0);
        fiefdomObj3.setLoyalVassals(new ArrayList<String>());
        Map.map.put(fiefdomObj3.getName(), fiefdomObj3);
        Collection<String> col3 = new ArrayList<String>(Arrays.asList(fiefdomObj3.getName()));
        vassalRebellion3.perform(col3);
        assertFalse(fiefdomObj3.isRebelliousVassals());
        assertEquals(1, RollEvent.rollEvents.size());
    }
}