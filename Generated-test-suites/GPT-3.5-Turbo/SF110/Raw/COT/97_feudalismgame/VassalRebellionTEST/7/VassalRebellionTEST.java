import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Create test data
        Fiefdoms fiefdom = new Fiefdoms();
        fiefdom.setPersonalCastles(2);
        fiefdom.setFiefdomCastles(3);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        fiefdom.setLoyalVassals(loyalVassals);
        Map.map.put("TestFiefdom", fiefdom);
        
        // Perform the action
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        
        // Verify the results
        // Assert statements for the expected results
    }
}