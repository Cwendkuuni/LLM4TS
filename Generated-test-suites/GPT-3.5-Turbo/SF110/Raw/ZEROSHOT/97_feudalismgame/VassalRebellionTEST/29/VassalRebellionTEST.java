import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms();
        Map.map.put("TestFiefdom", fiefdomObj);
    }

    @Test
    public void testPerform_WithLoyalVassalsAndSatisfyingCondition_ShouldSetRebelliousVassalsAndKingdom() {
        // Arrange
        fiefdomObj.getLoyalVassals().add(new LoyalVassal());
        fiefdomObj.setValue(10);

        // Act
        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        // Assert
        Assert.assertTrue(fiefdomObj.isRebelliousVassals());
        Assert.assertFalse(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerform_WithLoyalVassalsAndNotSatisfyingCondition_ShouldSetRebelliousVassals() {
        // Arrange
        fiefdomObj.getLoyalVassals().add(new LoyalVassal());
        fiefdomObj.setValue(5);

        // Act
        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        // Assert
        Assert.assertTrue(fiefdomObj.isRebelliousVassals());
    }

    @Test
    public void testPerform_WithPersonalCastlesAndFiefdomCastlesAndSatisfyingCondition_ShouldSetRebelliousVassals() {
        // Arrange
        fiefdomObj.getLoyalVassals().add(new LoyalVassal());
        fiefdomObj.setValue(20);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);

        // Act
        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        // Assert
        Assert.assertTrue(fiefdomObj.isRebelliousVassals());
    }

    @Test
    public void testPerform_WithNoLoyalVassals_ShouldCreateRollEvent() {
        // Arrange

        // Act
        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        // Assert
        // Verify that a RollEvent is created
        // ...
    }
}