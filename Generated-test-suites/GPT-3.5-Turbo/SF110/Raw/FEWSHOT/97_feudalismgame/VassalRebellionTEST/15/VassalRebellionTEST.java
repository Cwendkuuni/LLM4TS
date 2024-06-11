package src;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        Map.map.clear();
        
        // Test with loyal vassals
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("TestFiefdom");
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        fiefdomObj.setRebelliousVassals(0, false);
        fiefdomObj.setKingdom(true);
        
        Map.map.put("TestFiefdom", fiefdomObj);
        
        Collection<String> col1 = new ArrayList<>();
        col1.add("TestFiefdom");
        
        VassalRebellion vassalRebellion1 = new VassalRebellion();
        vassalRebellion1.perform(col1);
        
        assertEquals(0, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());
        
        // Test without loyal vassals
        Fiefdoms fiefdomObj2 = new Fiefdoms();
        fiefdomObj2.setName("TestFiefdom2");
        fiefdomObj2.setPersonalCastles(0);
        fiefdomObj2.setFiefdomCastles(0);
        fiefdomObj2.setRebelliousVassals(0, false);
        fiefdomObj2.setKingdom(true);
        
        Map.map.put("TestFiefdom2", fiefdomObj2);
        
        Collection<String> col2 = new ArrayList<>();
        col2.add("TestFiefdom2");
        
        VassalRebellion vassalRebellion2 = new VassalRebellion();
        vassalRebellion2.perform(col2);
        
        assertEquals(0, fiefdomObj2.getRebelliousVassals());
        assertTrue(fiefdomObj2.isKingdom());
    }
}