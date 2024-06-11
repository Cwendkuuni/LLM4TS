import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.Arrays;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_noLoyalVassals() {
        // Testing when there are no loyal vassals in the fiefdom
        vassalRebellion.perform(new ArrayList());
        // Assert that RollEvent is created
        assertEquals(1, Map.eventList.size());
    }

    @Test
    public void testPerform_rebelliousVassalsSet() {
        // Testing rebellious vassals set based on the conditions
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom");
        fiefdom.setPersonalCastles(1);
        fiefdom.setFiefdomCastles(2);
        fiefdom.setLoyalVassals(new ArrayList(Arrays.asList("Vassal1", "Vassal2")));
        Map.map.put("TestFiefdom", fiefdom);

        vassalRebellion.perform(new ArrayList(Arrays.asList("TestFiefdom")));
        // Assert that rebellious vassals are set
        assertEquals(1, fiefdom.getRebelliousVassals());
    }

    // Add more test cases as needed

}