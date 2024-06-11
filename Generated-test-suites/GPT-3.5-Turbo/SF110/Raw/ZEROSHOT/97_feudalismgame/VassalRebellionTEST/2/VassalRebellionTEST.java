package src;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Test case 1:
        Collection<String> col1 = new ArrayList<>();
        col1.add("Fiefdom 1");
        vassalRebellion.perform(col1);
        // Add assertions here
        
        // Test case 2:
        Collection<String> col2 = new ArrayList<>();
        col2.add("Fiefdom 2");
        vassalRebellion.perform(col2);
        // Add assertions here
        
        // Test case 3:
        Collection<String> col3 = new ArrayList<>();
        col3.add("Fiefdom 3");
        vassalRebellion.perform(col3);
        // Add assertions here
        
        // Test case 4:
        Collection<String> col4 = new ArrayList<>();
        col4.add("Fiefdom 4");
        vassalRebellion.perform(col4);
        // Add assertions here
        
        // Test case 5:
        Collection<String> col5 = new ArrayList<>();
        col5.add("Fiefdom 5");
        vassalRebellion.perform(col5);
        // Add assertions here
    }

    // Add more test methods here

}