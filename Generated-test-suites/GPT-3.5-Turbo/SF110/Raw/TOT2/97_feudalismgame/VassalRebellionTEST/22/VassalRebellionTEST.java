
import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case for typical use case
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        assertNotNull(vassalRebellion.getFiefdomObj());
        
        // Test case for edge case with empty loyal vassals
        vassalRebellion.perform(new ArrayList<>());
        assertNotNull(vassalRebellion.getFiefdomObj());
        
        // Test case for edge case with personal castles and fiefdom castles
        vassalRebellion.perform(col);
        vassalRebellion.getFiefdomObj().setPersonalCastles(10);
        vassalRebellion.getFiefdomObj().setFiefdomCastles(5);
        vassalRebellion.perform(col);
        assertTrue(vassalRebellion.getFiefdomObj().isRebelliousVassals());
    }
    
    @Test
    public void testGetFiefdomObj() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        assertNull(vassalRebellion.getFiefdomObj());
    }
    
    @Test
    public void testSetFiefdomObj() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdoms = new Fiefdoms();
        vassalRebellion.setFiefdomObj(fiefdoms);
        assertEquals(fiefdoms, vassalRebellion.getFiefdomObj());
    }
    
    @Test
    public void testGetFiefdoms() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        assertNull(vassalRebellion.getFiefdoms());
    }
    
    @Test
    public void testSetFiefdoms() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdoms = new Fiefdoms();
        vassalRebellion.setFiefdoms(fiefdoms);
        assertEquals(fiefdoms, vassalRebellion.getFiefdoms());
    }
}
