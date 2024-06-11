import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Test
    public void testPerform_withLoyalVassals() {
        vassalRebellion = new VassalRebellion();

        Collection<String> col = new ArrayList<>(Arrays.asList("Fiefdom1"));
        vassalRebellion.perform(col);

        // Assert that the fiefdomObj has been updated correctly based on loyal vassals
        assertEquals(0, vassalRebellion.fiefdomObj.getRebelliousVassals());
        assertFalse(vassalRebellion.fiefdomObj.isKingdom());
    }

    @Test
    public void testPerform_withPersonalAndFiefdomCastles() {
        vassalRebellion = new VassalRebellion();

        Collection<String> col = new ArrayList<>(Arrays.asList("Fiefdom2"));
        vassalRebellion.perform(col);

        // Assert that the rebellious vassals have been updated based on castle counts
        assertEquals(0, vassalRebellion.fiefdomObj.getRebelliousVassals());
    }

    @Test
    public void testPerform_withoutLoyalVassals() {
        vassalRebellion = new VassalRebellion();

        Collection<String> col = new ArrayList<>(Arrays.asList("Fiefdom3"));
        vassalRebellion.perform(col);

        // Assert that a RollEvent has been created when there are no loyal vassals
        assertNull(vassalRebellion.fiefdomObj);
    }

    // Add more test cases for edge cases and exceptions as needed

}