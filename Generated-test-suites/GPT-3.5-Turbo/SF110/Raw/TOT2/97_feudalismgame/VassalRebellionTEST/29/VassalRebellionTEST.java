import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerformWithRebelliousVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", new Fiefdoms());
        Map.map.get("TestFiefdom").setLoyalVassals(new ArrayList<>());
        Map.map.get("TestFiefdom").setPersonalCastles(2);
        Map.map.get("TestFiefdom").setFiefdomCastles(3);
        Map.map.get("TestFiefdom").setValue(1);

        Collection<String> testCollection = new ArrayList<>();
        testCollection.add("TestFiefdom");

        vassalRebellion.perform(testCollection);

        assertTrue(Map.map.get("TestFiefdom").isRebelliousVassals());
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", new Fiefdoms());
        Map.map.get("TestFiefdom").setLoyalVassals(new ArrayList<>());

        Collection<String> testCollection = new ArrayList<>();
        testCollection.add("TestFiefdom");

        vassalRebellion.perform(testCollection);

        assertNull(Map.map.get("TestFiefdom").getPersonalCastles());
        assertNull(Map.map.get("TestFiefdom").getFiefdomCastles());
        assertFalse(Map.map.get("TestFiefdom").isRebelliousVassals());
    }

    @Test
    public void testPerformWithNegativeVassalCount() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", new Fiefdoms());
        Map.map.get("TestFiefdom").setLoyalVassals(new ArrayList<>());
        Map.map.get("TestFiefdom").setValue(2);

        Collection<String> testCollection = new ArrayList<>();
        testCollection.add("TestFiefdom");

        vassalRebellion.perform(testCollection);

        assertEquals(0, Map.map.get("TestFiefdom").getRebelliousVassals());
    }

    @Test
    public void testPerformWithoutPersonalOrFiefdomCastles() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", new Fiefdoms());
        Map.map.get("TestFiefdom").setLoyalVassals(new ArrayList<>());
        Map.map.get("TestFiefdom").setPersonalCastles(0);
        Map.map.get("TestFiefdom").setFiefdomCastles(0);

        Collection<String> testCollection = new ArrayList<>();
        testCollection.add("TestFiefdom");

        vassalRebellion.perform(testCollection);

        assertEquals(0, Map.map.get("TestFiefdom").getRebelliousVassals());
    }
}