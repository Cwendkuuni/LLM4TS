import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms();
    }

    @Test
    public void testPerform() {
        // Test with loyal vassals present and enough loyal vassals to rebel
        vassalRebellion.perform(Arrays.asList("FiefdomName1"));
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());

        // Test with loyal vassals present and not enough loyal vassals to rebel
        fiefdomObj.setLoyalVassals(Arrays.asList("LoyalVassal1", "LoyalVassal2"));
        fiefdomObj.setValue(2);
        vassalRebellion.perform(Arrays.asList("FiefdomName2"));
        assertFalse(fiefdomObj.isRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());

        // Test with loyal vassals present and enough loyal vassals to rebel, but enough personal and fiefdom castles to prevent rebellion
        fiefdomObj.setValue(5);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        vassalRebellion.perform(Arrays.asList("FiefdomName3"));
        assertFalse(fiefdomObj.isRebelliousVassals());

        // Test with no loyal vassals present
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        vassalRebellion.perform(Arrays.asList("FiefdomName4"));
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }
}