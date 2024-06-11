import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        VassalRebellion rebellion = new VassalRebellion();
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom", 10, 5);
        Map.map.put("TestFiefdom", fiefdom);
        
        // Typical use case
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        rebellion.perform(col);
        assertFalse(fiefdom.isRebellious());
        
        // Edge case: No loyal vassals
        fiefdom.setLoyalVassals(new ArrayList<>());
        rebellion.perform(col);
        assertTrue(fiefdom.isRebellious());
        
        // Edge case: Difference between loyal vassals and value
        fiefdom.setLoyalVassals(new ArrayList<>());
        fiefdom.setValue(5);
        fiefdom.addVassal(new Vassal("TestVassal", 20));
        rebellion.perform(col);
        assertTrue(fiefdom.isRebellious());
        
        // Error scenario: Invalid fiefdom name
        col.clear();
        col.add("InvalidFiefdom");
        rebellion.perform(col);
        // Exception handling scenario: Check if RollEvent is created
        // Assertion to check if RollEvent for invalid fiefdom is not null
        assertNotNull(rebellion.fiefdomObj.getEvent());
    }
}