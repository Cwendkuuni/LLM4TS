package src;

import org.junit.*;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    
    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }
    
    @After
    public void tearDown() {
        vassalRebellion = null;
    }
    
    @Test
    public void testPerform() {
        // Test case 1
        Collection<String> col1 = new ArrayList<>();
        col1.add("Fiefdom1");
        vassalRebellion.perform(col1);
        // add assertions here
        
        // Test case 2
        Collection<String> col2 = new ArrayList<>();
        vassalRebellion.perform(col2);
        // add assertions here
    }
    
    // add more test methods here
    
}