package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        VassalRebellion vr = new VassalRebellion();

        // Test 1
        Collection<String> col1 = new ArrayList<>();
        col1.add("fiefdom1");
        Map.map.put("fiefdom1", new Fiefdoms("fiefdom1"));
        vr.perform(col1);
        Fiefdoms f1 = Map.map.get("fiefdom1");
        assertEquals(0, f1.getPersonalCastles());
        assertEquals(0, f1.getFiefdomCastles());
        assertFalse(f1.isKingdom());
        assertTrue(f1.getRebelliousVassals());
        assertFalse(f1.getLoyalVassals().isEmpty());

        // Test 2
        Collection<String> col2 = new ArrayList<>();
        col2.add("fiefdom2");
        Map.map.put("fiefdom2", new Fiefdoms("fiefdom2"));
        vr.perform(col2);
        Fiefdoms f2 = Map.map.get("fiefdom2");
        assertEquals(0, f2.getPersonalCastles());
        assertEquals(0, f2.getFiefdomCastles());
        assertFalse(f2.isKingdom());
        assertTrue(f2.getRebelliousVassals());
        assertFalse(f2.getLoyalVassals().isEmpty());

        // Test 3
        Collection<String> col3 = new ArrayList<>();
        col3.add("fiefdom3");
        Map.map.put("fiefdom3", new Fiefdoms("fiefdom3"));
        vr.perform(col3);
        Fiefdoms f3 = Map.map.get("fiefdom3");
        assertEquals(0, f3.getPersonalCastles());
        assertEquals(0, f3.getFiefdomCastles());
        assertFalse(f3.isKingdom());
        assertTrue(f3.getRebelliousVassals());
        assertFalse(f3.getLoyalVassals().isEmpty());

        // Test 4
        Collection<String> col4 = new ArrayList<>();
        col4.add("fiefdom4");
        Map.map.put("fiefdom4", new Fiefdoms("fiefdom4"));
        vr.perform(col4);
        Fiefdoms f4 = Map.map.get("fiefdom4");
        assertEquals(0, f4.getPersonalCastles());
        assertEquals(0, f4.getFiefdomCastles());
        assertFalse(f4.isKingdom());
        assertTrue(f4.getRebelliousVassals());
        assertFalse(f4.getLoyalVassals().isEmpty());

        // Test 5
        Collection<String> col5 = new ArrayList<>();
        col5.add("fiefdom5");
        Map.map.put("fiefdom5", new Fiefdoms("fiefdom5"));
        vr.perform(col5);
        Fiefdoms f5 = Map.map.get("fiefdom5");
        assertEquals(0, f5.getPersonalCastles());
        assertEquals(0, f5.getFiefdomCastles());
        assertFalse(f5.isKingdom());
        assertTrue(f5.getRebelliousVassals());
        assertFalse(f5.getLoyalVassals().isEmpty());
    }
}