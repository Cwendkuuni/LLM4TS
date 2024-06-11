import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        
        // Test when loyal vassals are present and rebellion is triggered
        assertEquals(true, vassalRebellion.fiefdomObj.isRebellious());
        
        // Test when loyal vassals are present but rebellion is not triggered
        // so new RollEvent should be created
        Collection<String> col2 = new ArrayList<>();
        col2.add("AnotherFiefdom");
        vassalRebellion.perform(col2);
        
        // Test when loyal vassals are not present, so new RollEvent should be created
        Collection<String> col3 = new ArrayList<>();
        vassalRebellion.perform(col3);
    }
}