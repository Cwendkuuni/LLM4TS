package src;

import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case 1
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");
        vassalRebellion.perform(col);
        
        // Add your assertions here
        
        // Test case 2
        VassalRebellion vassalRebellion2 = new VassalRebellion();
        Collection<String> col2 = new ArrayList<>();
        col2.add("fiefdom2");
        vassalRebellion2.perform(col2);
        
        // Add your assertions here
        
        // Test case 3
        // Add more test cases as needed
    }
}