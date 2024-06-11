import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

public class VassalRebellionTest {
    
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    
    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms();
        Map.map = new HashMap<String, Fiefdoms>();
    }
    
    @Test
    public void testPerformLoyalVassalsNotEmpty() {
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setValue(10);
        vassalRebellion.perform(fiefdomObj);
        assertTrue(fiefdomObj.getRebelliousVassals().size() > 0);
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerformLoyalVassalsEmpty() {
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        vassalRebellion.perform(fiefdomObj);
        assertEquals(1, Map.map.size());
    }
    
    @Test
    public void testPerformCastlesNotEmpty() {
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        vassalRebellion.perform(fiefdomObj);
        assertTrue(fiefdomObj.getRebelliousVassals().size() > 0);
    }
    
    @Test
    public void testPerformCastlesEmpty() {
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setPersonalCastles(0);
        fiefdomObj.setFiefdomCastles(0);
        vassalRebellion.perform(fiefdomObj);
        assertEquals(1, Map.map.size());
    }
}