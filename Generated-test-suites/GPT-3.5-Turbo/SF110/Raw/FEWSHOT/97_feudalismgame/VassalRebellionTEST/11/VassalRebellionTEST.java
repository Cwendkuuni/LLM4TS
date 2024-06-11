package src;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    private Map<String, Fiefdoms> map;
    private Collection<String> col;
    private String fiefdomName;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms();
        map = new HashMap<String, Fiefdoms>();
        col = new ArrayList<String>();
        fiefdomName = "testFiefdom";
        map.put(fiefdomName, fiefdomObj);
        col.add(fiefdomName);
    }

    @Test
    public void testPerform() {
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Case 1: Loyal vassals exist, fiefdomObj.getValue() >= fiefdomObj.getLoyalVassals().size()
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        fiefdomObj.setValue(5);
        vassalRebellion.perform(col);
        assertEquals(5, fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());

        // Case 2: Loyal vassals exist, fiefdomObj.getValue() < fiefdomObj.getLoyalVassals().size()
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        fiefdomObj.setValue(2);
        vassalRebellion.perform(col);
        assertEquals(2, fiefdomObj.getRebelliousVassals());

        // Case 3: personalCastles > 0 and fiefdomCastles > 0, fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25) > 0
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        fiefdomObj.setValue(10);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        vassalRebellion.perform(col);
        assertEquals(7, fiefdomObj.getRebelliousVassals());
        
        // Case 4: Loyal vassals do not exist
        fiefdomObj.setLoyalVassals(null);
        vassalRebellion.perform(col);
        // Verify that a new RollEvent is created for the fiefdomObj.getName()
        // Write assertions to verify RollEvent creation

    }
}