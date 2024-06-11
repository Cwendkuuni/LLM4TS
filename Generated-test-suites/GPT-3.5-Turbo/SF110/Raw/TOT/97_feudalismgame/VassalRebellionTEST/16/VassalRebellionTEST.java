import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_typical() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        Map.map.put("Fiefdom1", new Fiefdoms("Fiefdom1"));
        Map.map.get("Fiefdom1").setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        Map.map.get("Fiefdom1").setValue(10);
        Map.map.get("Fiefdom1").setKingdom(true);
        Map.map.get("Fiefdom1").setPersonalCastles(2);
        Map.map.get("Fiefdom1").setFiefdomCastles(5);

        // Act
        vr.perform(col);

        // Assert
        assertEquals(0, Map.map.get("Fiefdom1").getLoyalVassals().size());
        assertFalse(Map.map.get("Fiefdom1").isKingdom());
        assertTrue(Map.map.get("Fiefdom1").getRebelliousVassals().get("Vassal1"));
        assertTrue(Map.map.get("Fiefdom1").getRebelliousVassals().get("Vassal2"));
        assertEquals(0, Map.map.get("Fiefdom1").getPersonalCastles());
        assertEquals(0, Map.map.get("Fiefdom1").getFiefdomCastles());
    }

    @Test
    public void testPerform_noLoyalVassals() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom2");
        Map.map.put("Fiefdom2", new Fiefdoms("Fiefdom2"));
        Map.map.get("Fiefdom2").setPersonalCastles(3);

        // Act
        vr.perform(col);

        // Assert
        assertTrue(Map.map.get("Fiefdom2").getRebelliousVassals().isEmpty());
        assertTrue(Map.map.get("Fiefdom2").isKingdom());
        assertEquals(0, Map.map.get("Fiefdom2").getPersonalCastles());
    }

    @Test
    public void testPerform_negativeRebelliousVassals() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom3");
        Map.map.put("Fiefdom3", new Fiefdoms("Fiefdom3"));
        Map.map.get("Fiefdom3").setLoyalVassals(Arrays.asList("Vassal3"));
        Map.map.get("Fiefdom3").setValue(5);

        // Act
        vr.perform(col);

        // Assert
        assertEquals(1, Map.map.get("Fiefdom3").getLoyalVassals().size());
        assertTrue(Map.map.get("Fiefdom3").isKingdom());
        assertFalse(Map.map.get("Fiefdom3").getRebelliousVassals().get("Vassal3"));
        assertEquals(0, Map.map.get("Fiefdom3").getRebelliousVassals().size());
    }

    @Test
    public void testPerform_noCastles() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom4");
        Map.map.put("Fiefdom4", new Fiefdoms("Fiefdom4"));
        Map.map.get("Fiefdom4").setLoyalVassals(Arrays.asList("Vassal4"));
        Map.map.get("Fiefdom4").setValue(20);
        Map.map.get("Fiefdom4").setKingdom(true);
        Map.map.get("Fiefdom4").setPersonalCastles(0);
        Map.map.get("Fiefdom4").setFiefdomCastles(0);

        // Act
        vr.perform(col);

        // Assert
        assertEquals(0, Map.map.get("Fiefdom4").getLoyalVassals().size());
        assertFalse(Map.map.get("Fiefdom4").isKingdom());
        assertTrue(Map.map.get("Fiefdom4").getRebelliousVassals().get("Vassal4"));
        assertEquals(1, Map.map.get("Fiefdom4").getRebelliousVassals().size());
        assertEquals(0, Map.map.get("Fiefdom4").getPersonalCastles());
        assertEquals(0, Map.map.get("Fiefdom4").getFiefdomCastles());
    }

    @After
    public void cleanup() {
        Map.map.clear();
    }
}