import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class VassalRebellionTest {

    @Test
    public void performTest() {
        // Typical use case where loyal vassals rebel
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 1, 0, new ArrayList<>());
        Map.map.put("TestFiefdom", fiefdomObj);
        Collection<String> col = Arrays.asList("TestFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(col);
        
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        
        // Edge case where loyal vassals rebel with personal and fiefdom castles
        fiefdomObj.addPersonalCastles(1);
        fiefdomObj.addFiefdomCastles(1);
        fiefdomObj.addValue(10);
        col = Arrays.asList("TestFiefdom");
        vassalRebellion.perform(col);
        
        assertTrue(fiefdomObj.isRebelliousVassals());
        
        // Error scenario where no loyal vassals exist
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        col = Arrays.asList("TestFiefdom");
        vassalRebellion.perform(col);
        
        assertNull(fiefdomObj.getName());
    }
}