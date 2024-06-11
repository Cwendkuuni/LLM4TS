import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Typical use case
        VassalRebellion rebellion = new VassalRebellion();
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        rebellion.getMap().put("Fiefdom1", new Fiefdoms("Fiefdom1", 2, 3, loyalVassals));
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        rebellion.perform(col);
        assertEquals(2, rebellion.getMap().get("Fiefdom1").getRebelliousVassals().size());

        // Edge case: No loyal vassals
        Collection<String> col2 = new ArrayList<>();
        col2.add("Fiefdom1");
        rebellion.perform(col2);
        assertEquals(0, rebellion.getMap().get("Fiefdom1").getRebelliousVassals().size());

        // Error scenario: Null collection
        Collection<String> col3 = null;
        rebellion.perform(col3);
        assertNull(rebellion.getMap().get("Fiefdom1"));

        // Error scenario: Empty collection
        Collection<String> col4 = new ArrayList<>();
        rebellion.perform(col4);
        assertNull(rebellion.getMap().get("Fiefdom1"));
    }
}