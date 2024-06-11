package src;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;

class VassalRebellionTest {

    @Test
    void testPerform() {
        // Initialize variables for testing
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Collection<Object> col = new ArrayList<>();
        String fiefdomName = "Test Fiefdom";
        fiefdomObj.setName(fiefdomName);
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        Map.map.put(fiefdomName, fiefdomObj);
        col.add(fiefdomName);

        // Test perform() method
        vassalRebellion.perform(col);

        // Verify the results
        assertEquals(2, fiefdomObj.getPersonalCastles());
        assertEquals(3, fiefdomObj.getFiefdomCastles());
        assertEquals(0, fiefdomObj.getRebelliousVassals().size());
    }
}