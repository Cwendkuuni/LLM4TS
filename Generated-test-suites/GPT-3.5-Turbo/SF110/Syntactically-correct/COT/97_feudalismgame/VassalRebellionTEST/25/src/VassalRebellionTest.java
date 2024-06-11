import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class VassalRebellionTest {
    // Test perform() method with loyal vassals and enough rebellious points
    @Test
    public void testPerformWithLoyalVassalsAndEnoughRebelliousPoints() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = Collections.singleton("fiefdomName");
        Map.map.put("fiefdomName", new Fiefdoms());
        Map.map.get("fiefdomName").setLoyalVassals(Collections.singleton("loyalVassal"));
        Map.map.get("fiefdomName").setValue(10);

        // Act
        vassalRebellion.perform(col);

        // Assert
        Assert.assertEquals(10, Map.map.get("fiefdomName").getRebelliousVassals().size());
        Assert.assertFalse(Map.map.get("fiefdomName").isKingdom());
    }

    // Test perform() method with loyal vassals and not enough rebellious points
    @Test
    public void testPerformWithLoyalVassalsAndNotEnoughRebelliousPoints() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = Collections.singleton("fiefdomName");
        Map.map.put("fiefdomName", new Fiefdoms());
        Map.map.get("fiefdomName").setLoyalVassals(Collections.singleton("loyalVassal"));
        Map.map.get("fiefdomName").setValue(5);

        // Act
        vassalRebellion.perform(col);

        // Assert
        Assert.assertEquals(1, Map.map.get("fiefdomName").getRebelliousVassals().size());
        Assert.assertTrue(Map.map.get("fiefdomName").isKingdom());
    }

    // Test perform() method with personal castles and fiefdom castles
    @Test
    public void testPerformWithPersonalCastlesAndFiefdomCastles() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = Collections.singleton("fiefdomName");
        Map.map.put("fiefdomName", new Fiefdoms());
        Map.map.get("fiefdomName").setLoyalVassals(Collections.singleton("loyalVassal"));
        Map.map.get("fiefdomName").setPersonalCastles(1);
        Map.map.get("fiefdomName").setFiefdomCastles(2);

        // Act
        vassalRebellion.perform(col);

        // Assert
        Assert.assertEquals(7, Map.map.get("fiefdomName").getRebelliousVassals().size());
    }

    // Test perform() method with no loyal vassals
    @Test
    public void testPerformWithNoLoyalVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = Collections.singleton("fiefdomName");
        Map.map.put("fiefdomName", new Fiefdoms());

        // Act
        vassalRebellion.perform(col);

        // Assert
        // Verify that a RollEvent object is created
        // Assert.assertEquals(expectedValue, actualValue);
    }
}