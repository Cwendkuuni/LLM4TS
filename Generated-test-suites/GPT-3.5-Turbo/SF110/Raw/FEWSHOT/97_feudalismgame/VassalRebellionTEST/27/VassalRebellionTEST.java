package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform() {
        // Test case 1
        Map<String, Fiefdoms> map = new HashMap<>();
        Fiefdoms fiefdom = new Fiefdoms();
        fiefdom.setName("Fiefdom 1");
        fiefdom.setLoyalVassals(new ArrayList<>());
        fiefdom.setPersonalCastles(0);
        fiefdom.setFiefdomCastles(0);
        map.put(fiefdom.getName(), fiefdom);
        Map.setMap(map);
        Collection<String> col1 = new ArrayList<>();
        col1.add("Fiefdom 1");
        vassalRebellion.perform(col1);
        assertEquals(0, fiefdom.getRebelliousVassals());
        assertFalse(fiefdom.isKingdom());

        // Test case 2
        Fiefdoms fiefdom2 = new Fiefdoms();
        fiefdom2.setName("Fiefdom 2");
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal 1");
        loyalVassals.add("Vassal 2");
        loyalVassals.add("Vassal 3");
        fiefdom2.setLoyalVassals(loyalVassals);
        fiefdom2.setValue(2);
        map.put(fiefdom2.getName(), fiefdom2);
        Map.setMap(map);
        Collection<String> col2 = new ArrayList<>();
        col2.add("Fiefdom 2");
        vassalRebellion.perform(col2);
        assertEquals(2, fiefdom2.getRebelliousVassals());
        assertFalse(fiefdom2.isKingdom());

        // Test case 3
        Fiefdoms fiefdom3 = new Fiefdoms();
        fiefdom3.setName("Fiefdom 3");
        List<String> loyalVassals3 = new ArrayList<>();
        loyalVassals3.add("Vassal 1");
        loyalVassals3.add("Vassal 2");
        fiefdom3.setLoyalVassals(loyalVassals3);
        fiefdom3.setValue(2);
        fiefdom3.setPersonalCastles(0);
        fiefdom3.setFiefdomCastles(0);
        map.put(fiefdom3.getName(), fiefdom3);
        Map.setMap(map);
        Collection<String> col3 = new ArrayList<>();
        col3.add("Fiefdom 3");
        vassalRebellion.perform(col3);
        assertEquals(2, fiefdom3.getRebelliousVassals());
        assertFalse(fiefdom3.isKingdom());

        // Test case 4
        Fiefdoms fiefdom4 = new Fiefdoms();
        fiefdom4.setName("Fiefdom 4");
        List<String> loyalVassals4 = new ArrayList<>();
        loyalVassals4.add("Vassal 1");
        loyalVassals4.add("Vassal 2");
        loyalVassals4.add("Vassal 3");
        fiefdom4.setLoyalVassals(loyalVassals4);
        fiefdom4.setValue(3);
        fiefdom4.setPersonalCastles(1);
        fiefdom4.setFiefdomCastles(1);
        map.put(fiefdom4.getName(), fiefdom4);
        Map.setMap(map);
        Collection<String> col4 = new ArrayList<>();
        col4.add("Fiefdom 4");
        vassalRebellion.perform(col4);
        assertEquals(0, fiefdom4.getRebelliousVassals());
        assertFalse(fiefdom4.isKingdom());

        // Test case 5
        Collection<String> col5 = new ArrayList<>();
        vassalRebellion.perform(col5);
        assertNotNull(Map.map.get("Fiefdom 3"));
        assertNotEquals(0, Map.map.get("Fiefdom 3").getLoyalVassals().size());
    }
}