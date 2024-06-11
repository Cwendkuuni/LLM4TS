import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;

public class VassalRebellionTest {
    
    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", new Fiefdoms("TestFiefdom"));
        
        // Typical use case
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        
        // Edge case
        Fiefdoms fiefdom = Map.map.get("TestFiefdom");
        fiefdom.setPersonalCastles(1);
        fiefdom.setFiefdomCastles(0);
        fiefdom.setLoyalVassals(new ArrayList<>());
        vassalRebellion.perform(col);
        
        // Error scenario
        Collection<String> emptyCol = new ArrayList<>();
        Exception exception = assertThrows(NoSuchElementException.class, () -> vassalRebellion.perform(emptyCol));
        assertEquals("No value present", exception.getMessage());
    }
    
}