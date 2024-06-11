import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform_WhenCollectionIsEmpty() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.emptyList());

        // Assert expected behavior
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_WhenFiefdomNameNotInMap() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.singletonList("UnknownFiefdomName"));

        // Assert expected exception
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_WhenFiefdomObjIsNull() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.singletonList("ValidFiefdomName"));

        // Assert expected exception
    }

    @Test
    public void testPerform_WhenPersonalCastlesIsNegative() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.singletonList("ValidFiefdomName"));

        // Assert expected behavior
    }

    @Test
    public void testPerform_WhenFiefdomCastlesIsNegative() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.singletonList("ValidFiefdomName"));

        // Assert expected behavior
    }

    @Test
    public void testPerform_WhenLoyalVassalsSizeLessThanRebelliousVassals() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.singletonList("ValidFiefdomName"));

        // Assert expected behavior
    }
}