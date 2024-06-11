import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        // Typical use case
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        
        // Edge cases
        // Test when no loyal vassals present
        VassalRebellion vassalRebellionNoVassals = new VassalRebellion();
        Collection<String> colNoVassals = new ArrayList<>();
        colNoVassals.add("NoVassalsFiefdom");
        vassalRebellionNoVassals.perform(colNoVassals);
        
        // Test when loyal vassals size is greater than value
        VassalRebellion vassalRebellionLargeVassals = new VassalRebellion();
        Collection<String> colLargeVassals = new ArrayList<>();
        colLargeVassals.add("LargeVassalsFiefdom");
        vassalRebellionLargeVassals.perform(colLargeVassals);
        
        // Error scenario
        // Test for NullPointerException if fiefdomName is null
        VassalRebellion vassalRebellionNullFiefdom = new VassalRebellion();
        Collection<String> colNullFiefdom = new ArrayList<>();
        colNullFiefdom.add(null);
        vassalRebellionNullFiefdom.perform(colNullFiefdom);
    }
}