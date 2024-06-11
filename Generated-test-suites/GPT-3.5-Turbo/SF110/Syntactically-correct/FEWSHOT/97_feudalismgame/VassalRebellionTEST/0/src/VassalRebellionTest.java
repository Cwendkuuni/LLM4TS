package src;

import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {
    @Test
    public void testPerform() {
        // Test case 1
        Collection<String> col1 = new ArrayList<>();
        col1.add("fiefdom1");
        assertEquals(0, Map.map.get("fiefdom1").getRebelliousVassals());
        new VassalRebellion().perform(col1);
        assertEquals(0, Map.map.get("fiefdom1").getRebelliousVassals());
        
        // Test case 2
        Collection<String> col2 = new ArrayList<>();
        col2.add("fiefdom2");
        Map.map.get("fiefdom2").addLoyalVassal("vassal1");
        Map.map.get("fiefdom2").addLoyalVassal("vassal2");
        Map.map.get("fiefdom2").addLoyalVassal("vassal3");
        Map.map.get("fiefdom2").addLoyalVassal("vassal4");
        Map.map.get("fiefdom2").setValue(4);
        assertEquals(0, Map.map.get("fiefdom2").getRebelliousVassals());
        new VassalRebellion().perform(col2);
        assertEquals(4, Map.map.get("fiefdom2").getRebelliousVassals());
        
        // Test case 3
        Collection<String> col3 = new ArrayList<>();
        col3.add("fiefdom3");
        Map.map.get("fiefdom3").addLoyalVassal("vassal1");
        Map.map.get("fiefdom3").addLoyalVassal("vassal2");
        Map.map.get("fiefdom3").addLoyalVassal("vassal3");
        Map.map.get("fiefdom3").addLoyalVassal("vassal4");
        Map.map.get("fiefdom3").setValue(2);
        assertEquals(0, Map.map.get("fiefdom3").getRebelliousVassals());
        new VassalRebellion().perform(col3);
        assertEquals(2, Map.map.get("fiefdom3").getRebelliousVassals());
        
        // Test case 4
        Collection<String> col4 = new ArrayList<>();
        col4.add("fiefdom4");
        Map.map.get("fiefdom4").addLoyalVassal("vassal1");
        Map.map.get("fiefdom4").addLoyalVassal("vassal2");
        Map.map.get("fiefdom4").addLoyalVassal("vassal3");
        Map.map.get("fiefdom4").addLoyalVassal("vassal4");
        Map.map.get("fiefdom4").setValue(5);
        Map.map.get("fiefdom4").setPersonalCastles(1);
        Map.map.get("fiefdom4").setFiefdomCastles(1);
        assertEquals(0, Map.map.get("fiefdom4").getRebelliousVassals());
        new VassalRebellion().perform(col4);
        assertEquals(1, Map.map.get("fiefdom4").getRebelliousVassals());
        
        // Test case 5
        Collection<String> col5 = new ArrayList<>();
        col5.add("fiefdom5");
        Map.map.get("fiefdom5").setLoyalVassals(new ArrayList<String>());
        assertEquals(0, Map.map.get("fiefdom5").getRebelliousVassals());
        new VassalRebellion().perform(col5);
        assertEquals(0, Map.map.get("fiefdom5").getRebelliousVassals());
    }
}