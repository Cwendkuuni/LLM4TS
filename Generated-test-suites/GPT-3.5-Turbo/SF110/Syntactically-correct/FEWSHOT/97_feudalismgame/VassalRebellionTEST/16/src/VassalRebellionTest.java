package src;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case 1
        Collection<String> col1 = new ArrayList<String>();
        col1.add("fiefdomName");
        Map<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("fiefdomName");
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setRebelliousVassals(0, false);
        fiefdomObj.setValue(0);
        fiefdomObj.setKingdom(true);
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        map.put("fiefdomName", fiefdomObj);
        Map.setMap(map);
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(col1);
        assertEquals(0, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());

        // Test case 2
        Collection<String> col2 = new ArrayList<String>();
        col2.add("fiefdomName");
        ArrayList<String> loyalVassals2 = new ArrayList<String>();
        loyalVassals2.add("loyalVassal1");
        loyalVassals2.add("loyalVassal2");
        fiefdomObj.setLoyalVassals(loyalVassals2);
        fiefdomObj.setValue(3);
        vassalRebellion.perform(col2);
        assertEquals(3, fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        
        // Test case 3
        Collection<String> col3 = new ArrayList<String>();
        col3.add("fiefdomName");
        fiefdomObj.setValue(0);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        vassalRebellion.perform(col3);
        assertEquals(3, fiefdomObj.getRebelliousVassals());
        
        // Test case 4
        Collection<String> col4 = new ArrayList<String>();
        col4.add("otherFiefdomName");
        vassalRebellion.perform(col4);
        assertEquals(0, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());
    }
}