package src;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();

        // Test case 1: Loyal Vassals present, enough vassals for rebellion
        fiefdomObj.setLoyalVassals(Arrays.asList("vassal1", "vassal2", "vassal3"));
        fiefdomObj.setValue(2);
        vassalRebellion.perform(Collections.singletonList("fiefdom1"));
        assertEquals(2, fiefdomObj.getRebelliousVassals().size());
        assertEquals(false, fiefdomObj.isKingdom());

        // Test case 2: Loyal Vassals present, not enough vassals for rebellion
        fiefdomObj.setLoyalVassals(Arrays.asList("vassal1", "vassal2", "vassal3"));
        fiefdomObj.setValue(5);
        vassalRebellion.perform(Collections.singletonList("fiefdom1"));
        assertEquals(3, fiefdomObj.getRebelliousVassals().size());
        assertEquals(true, fiefdomObj.isKingdom());

        // Test case 3: Personal and fiefdom castles present, not enough vassals for rebellion
        fiefdomObj.setLoyalVassals(Arrays.asList("vassal1", "vassal2", "vassal3"));
        fiefdomObj.setValue(3);
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(1);
        vassalRebellion.perform(Collections.singletonList("fiefdom1"));
        assertEquals(2, fiefdomObj.getRebelliousVassals().size());
        assertEquals(true, fiefdomObj.isKingdom());

        // Test case 4: No Loyal Vassals present
        fiefdomObj.setLoyalVassals(Collections.emptyList());
        vassalRebellion.perform(Collections.singletonList("fiefdom1"));
        assertEquals(0, fiefdomObj.getRebelliousVassals().size());
        assertEquals(true, fiefdomObj.isKingdom());
    }
}