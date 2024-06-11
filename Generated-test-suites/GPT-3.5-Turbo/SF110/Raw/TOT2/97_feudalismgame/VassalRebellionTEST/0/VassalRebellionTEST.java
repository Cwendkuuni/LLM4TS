import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Typical Use Case
        Collection<String> col = Arrays.asList("Fiefdom1");
        VassalRebellion rebellion = new VassalRebellion();
        rebellion.perform(col);
        
        // Edge Case: No loyal vassals
        Collection<String> col2 = Arrays.asList("Fiefdom2");
        rebellion.perform(col2);
        
        // Edge Case: Negative loyal vassals
        Collection<String> col3 = Arrays.asList("Fiefdom3");
        rebellion.perform(col3);
        
        // Edge Case: Negative personal castles and fiefdom castles
        Collection<String> col4 = Arrays.asList("Fiefdom4");
        rebellion.perform(col4);
        
        // Error Scenario: Empty Collection
        Collection<String> col5 = new ArrayList<>();
        rebellion.perform(col5);
        
        // Additional Error Scenario: Collection with multiple fiefdoms
        Collection<String> col6 = Arrays.asList("FiefdomA", "FiefdomB");
        rebellion.perform(col6);
    }
}