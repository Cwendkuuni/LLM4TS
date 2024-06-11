package src;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case 1
        Map<String, Fiefdoms> testMap = new HashMap<>();
        Fiefdoms f1 = new Fiefdoms();
        f1.setName("F1");
        f1.setPersonalCastles(1);
        f1.setFiefdomCastles(1);
        f1.setValue(2);
        f1.setLoyalVassals(new ArrayList<>());
        testMap.put("F1", f1);

        Collection<String> col1 = new ArrayList<>();
        col1.add("F1");

        VassalRebellion vr1 = new VassalRebellion();
        vr1.perform(col1);

        assertEquals(0, f1.getLoyalVassals().size());
        assertTrue(f1.getRebelliousVassals().get("F1"));
        assertFalse(f1.isKingdom());

        // Test case 2
        Map<String, Fiefdoms> testMap2 = new HashMap<>();
        Fiefdoms f2 = new Fiefdoms();
        f2.setName("F2");
        f2.setPersonalCastles(0);
        f2.setFiefdomCastles(0);
        f2.setValue(3);
        f2.setLoyalVassals(new ArrayList<>());
        testMap2.put("F2", f2);

        Collection<String> col2 = new ArrayList<>();
        col2.add("F2");

        VassalRebellion vr2 = new VassalRebellion();
        vr2.perform(col2);

        assertEquals(0, f2.getLoyalVassals().size());
        assertTrue(f2.getRebelliousVassals().get("F2"));
        assertTrue(f2.getRebelliousVassals().get("F2"));
        assertFalse(f2.isKingdom());

        // Test case 3
        Map<String, Fiefdoms> testMap3 = new HashMap<>();
        Fiefdoms f3 = new Fiefdoms();
        f3.setName("F3");
        f3.setPersonalCastles(2);
        f3.setFiefdomCastles(3);
        f3.setValue(10);
        f3.setLoyalVassals(new ArrayList<>());
        testMap3.put("F3", f3);

        Collection<String> col3 = new ArrayList<>();
        col3.add("F3");

        VassalRebellion vr3 = new VassalRebellion();
        vr3.perform(col3);

        assertEquals(0, f3.getLoyalVassals().size());
        assertTrue(f3.getRebelliousVassals().get("F3"));
        assertFalse(f3.isKingdom());
        assertEquals(0, f3.getLoyalVassals().size() - (f3.getPersonalCastles() * 10 + f3.getFiefdomCastles() * 25));
    }
}