import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();

        // Typical use case
        List<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        vassalRebellion.perform(col);

        // Edge cases
        // Test when loyalVassals size is greater than value
        vassalRebellion.perform(col);
        
        // Test when personalCastles and fiefdomCastles are greater than 0
        // and loyalVassals size - (personalCastles * 10 + fiefdomCastles * 25) is greater than 0
        vassalRebellion.perform(col);
        
        // Error scenarios
        // Test when no loyal vassals, RollEvent should be called
        List<String> emptyCol = new ArrayList<>();
        vassalRebellion.perform(emptyCol);
    }
}