package src;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        VassalRebellion rebellion = new VassalRebellion();
        
        // Test with non-empty loyal vassals
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        rebellion.perform(fiefdomObj);
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        
        // Test with loyal vassals less than value
        Fiefdoms fiefdomObj2 = new Fiefdoms();
        fiefdomObj2.setPersonalCastles(0);
        fiefdomObj2.setFiefdomCastles(0);
        fiefdomObj2.setLoyalVassals(new ArrayList<>());
        rebellion.perform(fiefdomObj2);
        assertTrue(fiefdomObj2.isRebelliousVassals());
        
        // Test with loyal vassals greater than value and non-zero castles
        Fiefdoms fiefdomObj3 = new Fiefdoms();
        fiefdomObj3.setPersonalCastles(1);
        fiefdomObj3.setFiefdomCastles(1);
        List<String> loyalVassals = Arrays.asList("vassal1", "vassal2", "vassal3");
        fiefdomObj3.setLoyalVassals(loyalVassals);
        rebellion.perform(fiefdomObj3);
        assertTrue(fiefdomObj3.isRebelliousVassals());
    }
}