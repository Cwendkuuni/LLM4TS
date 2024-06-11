import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map<String, Fiefdoms> testMap = new HashMap<>();
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 5, 3, new ArrayList<>());
        testMap.put("TestFiefdom", fiefdomObj);
        Map.map = testMap;
        
        List<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        
        vassalRebellion.perform(col);
        
        // Test case for fiefdomObj.getRebelliousVassals()
        assertEquals(3, fiefdomObj.getRebelliousVassals());
        
        // Test case for other scenarios and edge cases can be added here
    }
}