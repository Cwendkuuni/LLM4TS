package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_withLoyalVassalsAndEnoughRebelliousVassals_setRebelliousVassals() {
        HashMap<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        ArrayList<String> loyalVassals = new ArrayList<String>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setValue(2);
        map.put("Fiefdom1", fiefdomObj);
        Map.setMap(map);

        ArrayList<String> col = new ArrayList<String>();
        col.add("Fiefdom1");
        vassalRebellion.perform(col);

        assertEquals(2, fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerform_withLoyalVassalsAndNotEnoughRebelliousVassals_setRebelliousVassals() {
        HashMap<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        ArrayList<String> loyalVassals = new ArrayList<String>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setValue(4);
        map.put("Fiefdom1", fiefdomObj);
        Map.setMap(map);

        ArrayList<String> col = new ArrayList<String>();
        col.add("Fiefdom1");
        vassalRebellion.perform(col);

        assertEquals(2, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerform_withPersonalCastlesAndEnoughRebelliousVassals_setRebelliousVassals() {
        HashMap<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        ArrayList<String> loyalVassals = new ArrayList<String>();
        fiefdomObj.setLoyalVassals(loyalVassals);
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(1);
        map.put("Fiefdom1", fiefdomObj);
        Map.setMap(map);

        ArrayList<String> col = new ArrayList<String>();
        col.add("Fiefdom1");
        vassalRebellion.perform(col);

        assertEquals(2, fiefdomObj.getRebelliousVassals());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerform_withNoLoyalVassals_createRollEvent() {
        HashMap<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdomObj = new Fiefdoms();
        map.put("Fiefdom1", fiefdomObj);
        Map.setMap(map);

        ArrayList<String> col = new ArrayList<String>();
        col.add("Fiefdom1");
        vassalRebellion.perform(col);

        assertNull(fiefdomObj.getLoyalVassals());
    }

    // Add more test cases as needed

}