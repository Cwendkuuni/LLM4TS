package src;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        // Create sample data
        Map<String, Fiefdoms> fiefdomMap = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("Test Fiefdom");
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        fiefdomObj.setValue(5);
        fiefdomMap.put("Test Fiefdom", fiefdomObj);
        
        Collection<String> col = new ArrayList<>();
        col.add("Test Fiefdom");
        
        // Test when fiefdom has loyal vassals
        fiefdomObj.getLoyalVassals().add("Vassal 1");
        fiefdomObj.getLoyalVassals().add("Vassal 2");
        
        VassalRebellion rebellion = new VassalRebellion();
        rebellion.perform(col);
        
        assertTrue(fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        
        // Test when fiefdom has no loyal vassals
        fiefdomObj.getLoyalVassals().clear();
        
        ArrayList<Fiefdoms> rollEventList = new ArrayList<>();
        RollEvent.rollEventList = rollEventList;
        
        rebellion.perform(col);
        
        assertNull(fiefdomObj.getRebelliousVassals());
        assertEquals(1, rollEventList.size());
    }
}