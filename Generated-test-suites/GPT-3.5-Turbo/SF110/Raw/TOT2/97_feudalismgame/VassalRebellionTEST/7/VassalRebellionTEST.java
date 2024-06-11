import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class VassalRebellionTest {
    
    private VassalRebellion vassalRebellion;
    
    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }
    
    @Test
    public void testPerform() {
        // Typical use case
        vassalRebellion.perform(Arrays.asList("Fiefdom1"));
        
        // Edge case where loyal vassals - fiefdom value = 0
        vassalRebellion.perform(Arrays.asList("Fiefdom2"));
        
        // Edge case where personal castles and fiefdom castles are greater than 0
        vassalRebellion.perform(Arrays.asList("Fiefdom3"));
        
        // Error scenario where loyal vassals size is 0
        vassalRebellion.perform(Arrays.asList("Fiefdom4"));
    }
}