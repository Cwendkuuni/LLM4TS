import static org.junit.Assert.*;
import org.junit.Test;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection collection = new ArrayList<>();
        collection.add("Fiefdom1");
        vassalRebellion.perform(collection);
        // TODO: Write assertions to test the functionality of the perform method
    }
    
    // TODO: Write additional test cases to cover potential edge cases and exceptions
    
}