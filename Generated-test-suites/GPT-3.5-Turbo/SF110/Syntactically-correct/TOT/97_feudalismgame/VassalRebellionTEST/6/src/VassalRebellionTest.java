import org.junit.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_rebelliousVassalsSetToValueWhenEnoughLoyalVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        Map.map.put("Fiefdom1", new Fiefdoms());
        Map.map.get("Fiefdom1").setValue(5);
        Map.map.get("Fiefdom1").setLoyalVassals(new ArrayList<>());

        // Act
        vassalRebellion.perform(col);

        // Assert
        Assert.assertEquals(5, Map.map.get("Fiefdom1").getRebelliousVassals());
        Assert.assertFalse(Map.map.get("Fiefdom1").isKingdom());
    }

    @Test
    public void testPerform_rebelliousVassalsSetToLoyalVassalsSizeWhenInsufficientLoyalVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom2");
        Map.map.put("Fiefdom2", new Fiefdoms());
        Map.map.get("Fiefdom2").setValue(5);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        Map.map.get("Fiefdom2").setLoyalVassals(loyalVassals);

        // Act
        vassalRebellion.perform(col);

        // Assert
        Assert.assertEquals(1, Map.map.get("Fiefdom2").getRebelliousVassals());
    }

    @Test
    public void testPerform_rebelliousVassalsSetToLoyalVassalsSizeMinusPersonalAndFiefdomCastlesWhenEnoughLoyalVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom3");
        Map.map.put("Fiefdom3", new Fiefdoms());
        Map.map.get("Fiefdom3").setValue(5);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        Map.map.get("Fiefdom3").setLoyalVassals(loyalVassals);
        Map.map.get("Fiefdom3").setPersonalCastles(1);
        Map.map.get("Fiefdom3").setFiefdomCastles(4);

        // Act
        vassalRebellion.perform(col);

        // Assert
        Assert.assertEquals(0, Map.map.get("Fiefdom3").getRebelliousVassals());
    }

    @Test
    public void testPerform_rebelliousVassalsNotSetWhenNoLoyalVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom4");
        Map.map.put("Fiefdom4", new Fiefdoms());
        Map.map.get("Fiefdom4").setLoyalVassals(new ArrayList<>());

        // Act
        vassalRebellion.perform(col);

        // Assert
        // Check that the RollEvent constructor was called
        // Not tested here, will be tested in another test case for RollEvent class
    }
}