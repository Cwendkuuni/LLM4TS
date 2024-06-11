import org.junit.Test;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion = new VassalRebellion();

    @Test
    public void testPerform_withNonRebelliousVassals_shouldNotSetRebelliousVassals() {
        Fiefdoms fiefdoms = new Fiefdoms();
        fiefdoms.setName("Test Fiefdom");
        fiefdoms.setLoyalVassals(new ArrayList<>());
        fiefdoms.setPersonalCastles(3);
        fiefdoms.setFiefdomCastles(5);
        Map.map.put(fiefdoms.getName(), fiefdoms);

        vassalRebellion.perform(Arrays.asList(fiefdoms.getName()));

        assertFalse(fiefdoms.isRebelliousVassals());
    }

    @Test
    public void testPerform_withRebelliousVassals_shouldSetRebelliousVassals() {
        Fiefdoms fiefdoms = new Fiefdoms();
        fiefdoms.setName("Test Fiefdom");
        fiefdoms.setLoyalVassals(new ArrayList<>());
        fiefdoms.setPersonalCastles(12);
        fiefdoms.setFiefdomCastles(8);
        Map.map.put(fiefdoms.getName(), fiefdoms);

        vassalRebellion.perform(Arrays.asList(fiefdoms.getName()));

        assertTrue(fiefdoms.isRebelliousVassals());
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_withNullCollection_shouldThrowNullPointerException() {
        vassalRebellion.perform(null);
    }

    @Test
    public void testPerform_withEmptyCollection_shouldNotThrowException() {
        vassalRebellion.perform(new ArrayList<>());
    }

    @Test
    public void testPerform_withLoyalVassalsAndZeroPersonalAndFiefdomCastles_shouldSetRebelliousVassals() {
        Fiefdoms fiefdoms = new Fiefdoms();
        fiefdoms.setName("Test Fiefdom");
        fiefdoms.setLoyalVassals(new ArrayList<>());
        fiefdoms.setPersonalCastles(0);
        fiefdoms.setFiefdomCastles(0);
        fiefdoms.setValue(10);
        Map.map.put(fiefdoms.getName(), fiefdoms);

        vassalRebellion.perform(Arrays.asList(fiefdoms.getName()));

        assertTrue(fiefdoms.isRebelliousVassals());
    }
}