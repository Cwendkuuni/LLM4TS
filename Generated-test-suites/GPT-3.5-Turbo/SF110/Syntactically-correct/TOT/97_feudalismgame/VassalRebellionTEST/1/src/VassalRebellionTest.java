import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void perform_validLoyalVassals_rebelliousVassalsSet() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("TestFiefdom", fiefdomObj);
        fiefdomObj.setLoyalVassals(new ArrayList<>()); //Empty loyal vassals
        fiefdomObj.setValue(5);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(3, fiefdomObj.getRebelliousVassals().size());
    }

    @Test
    public void perform_emptyLoyalVassals_produceRollEvent() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("TestFiefdom", fiefdomObj);
        fiefdomObj.setLoyalVassals(new ArrayList<>()); //Empty loyal vassals

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        // Check if RollEvent is produced
        assertEquals(1, RollEvent.getRollEvents().size());
        assertEquals("TestFiefdom", RollEvent.getRollEvents().get(0));
    }

    @Test
    public void perform_invalidLoyalVassals_rebelliousVassalsSet() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        Map.map.put("TestFiefdom", fiefdomObj);
        fiefdomObj.setLoyalVassals(new ArrayList<>()); //Empty loyal vassals to trigger wrong if condition
        fiefdomObj.setValue(3); // Set value to 3
        fiefdomObj.setPersonalCastles(0); // Set personal castles to 0
        fiefdomObj.setFiefdomCastles(0); // Set fiefdom castles to 0

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(3, fiefdomObj.getRebelliousVassals().size());
    }
}