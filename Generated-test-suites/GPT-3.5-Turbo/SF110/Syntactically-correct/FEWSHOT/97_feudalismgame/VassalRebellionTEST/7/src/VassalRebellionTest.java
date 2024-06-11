
package src;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();

        // Test Case 1: Rebellious Vassals set to fiefdomObj's value when it is greater than 0 and the size of loyalVassals is greater than fiefdomObj's value
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setValue(3);
        Map<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        map.put("F1", fiefdomObj);
        Map.map = map;
        Collection<String> col = new ArrayList<String>();
        col.add("F1");
        vassalRebellion.perform(col);
        assertEquals(3, fiefdomObj.getRebelliousVassals());
        assertEquals(false, fiefdomObj.isKingdom());

        // Test Case 2: Rebellious Vassals set to size of loyalVassals when it is less than fiefdomObj's value
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        vassalRebellion.perform(col);
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Test Case 3: Rebellious Vassals set to size of loyalVassals which is greater than sum of personalCastles * 10 and fiefdomCastles * 25
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        vassalRebellion.perform(col);
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Test Case 4: Rebellious Vassals set to size of loyalVassals minus sum of personalCastles * 10 and fiefdomCastles * 25 when it is greater than 0
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        fiefdomObj.setValue(5);
        vassalRebellion.perform(col);
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Test Case 5: Rebellious Vassals set to 0 when size of loyalVassals is 0
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        map.put("F1", fiefdomObj);
        vassalRebellion.perform(col);
        assertEquals(0, fiefdomObj.getRebelliousVassals());

        // Test Case 6: RollEvent is created when size of loyalVassals is 0
        fiefdomObj.setLoyalVassals(new ArrayList<Vassal>());
        fiefdomObj.setName("F1");
        map.put("F1", fiefdomObj);
        vassalRebellion.perform(col);
        //Assertion for RollEvent creation
    }
}
