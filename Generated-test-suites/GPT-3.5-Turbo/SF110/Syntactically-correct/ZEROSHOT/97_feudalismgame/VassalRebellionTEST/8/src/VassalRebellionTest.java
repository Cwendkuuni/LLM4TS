package src;

import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Create instance of VassalRebellion
        VassalRebellion vassalRebellion = new VassalRebellion();

        // Setup test data
        String fiefdomName = "Test Fiefdom";
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put(fiefdomName, fiefdomObj);

        // Set initial values for fiefdomObj
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        fiefdomObj.setKingdom(true);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(10);

        // Call perform method
        vassalRebellion.perform(Collections.singletonList(fiefdomName));

        // Test the result
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Change values for fiefdomObj
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        fiefdomObj.setKingdom(true);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(10);

        // Call perform method
        vassalRebellion.perform(Collections.singletonList(fiefdomName));

        // Test the result
        assertEquals(1, fiefdomObj.getRebelliousVassals());

        // Change values for fiefdomObj
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        fiefdomObj.setKingdom(true);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(5);

        // Call perform method
        vassalRebellion.perform(Collections.singletonList(fiefdomName));

        // Test the result
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Change values for fiefdomObj
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        fiefdomObj.setKingdom(false);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(10);

        // Call perform method
        vassalRebellion.perform(Collections.singletonList(fiefdomName));

        // Test the result
        assertEquals(0, fiefdomObj.getRebelliousVassals());
    }
}