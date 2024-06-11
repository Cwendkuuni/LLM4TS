package src;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case 1
        // Create an instance of VassalRebellion
        VassalRebellion vassalRebellion = new VassalRebellion();
        
        // Create a Collection object with a single element
        Collection<String> col1 = new ArrayList<>();
        col1.add("fiefdomName1");
        
        // Call the perform() method
        vassalRebellion.perform(col1);
        
        // Assertion statements
        assertEquals(0, vassalRebellion.fiefdomObj.getValue());
        assertEquals(true, vassalRebellion.fiefdomObj.isRebellious());
        assertEquals(false, vassalRebellion.fiefdomObj.isKingdom());
        
        // Test case 2
        // Create a Collection object with no elements
        Collection<String> col2 = new ArrayList<>();
        
        // Call the perform() method
        vassalRebellion.perform(col2);
        
        // Assertion statements
        assertEquals("fiefdomName1", vassalRebellion.fiefdomObj.getName());
    }
}