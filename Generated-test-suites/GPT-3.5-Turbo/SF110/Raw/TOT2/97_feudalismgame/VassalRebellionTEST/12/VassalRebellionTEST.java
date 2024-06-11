import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Typical use case
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        
        // Edge case: col is empty
        Collection<String> emptyCol = new ArrayList<>();
        vassalRebellion.perform(emptyCol);
        
        // Edge case: fiefdom has no loyal vassals
        Collection<String> noLoyalVassalsCol = new ArrayList<>();
        noLoyalVassalsCol.add("NoVassalsFiefdom");
        vassalRebellion.perform(noLoyalVassalsCol);
        
        // Error scenario: fiefdom name not in map
        Collection<String> invalidFiefdomCol = new ArrayList<>();
        invalidFiefdomCol.add("InvalidFiefdom");
        vassalRebellion.perform(invalidFiefdomCol);
        
        // Add more test cases for other scenarios
        
    }
}