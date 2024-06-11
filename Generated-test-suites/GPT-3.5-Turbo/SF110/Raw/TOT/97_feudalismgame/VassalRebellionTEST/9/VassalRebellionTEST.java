import org.junit.*;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_noLoyalVassals_rollEvent() {
        VassalRebellion rebellion = new VassalRebellion();
        rebellion.perform(new ArrayList<>());
        
        // Assert that a RollEvent object is created
        Assert.assertTrue(rebellion.fiefdomObj.getName().equals("RollEvent"));
        
        // Assert that the fiefdom's rebellious vassals are not set
        Assert.assertFalse(rebellion.fiefdomObj.isRebelliousVassals());
        
        // Assert that the fiefdom's kingdom status remains unchanged
        Assert.assertTrue(rebellion.fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_rebelliousVassalsSet() {
        VassalRebellion rebellion = new VassalRebellion();
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        rebellion.perform(loyalVassals);
        
        // Assert that the fiefdom's rebellious vassals are set to the number of loyal vassals
        Assert.assertTrue(rebellion.fiefdomObj.isRebelliousVassals());
        Assert.assertEquals(rebellion.fiefdomObj.getValue(), loyalVassals.size());
        
        // Assert that the fiefdom's kingdom status is set to false
        Assert.assertFalse(rebellion.fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_rebelliousVassalsSetWithPersonalAndFiefdomCastles() {
        VassalRebellion rebellion = new VassalRebellion();
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        loyalVassals.add("Vassal2");
        rebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        rebellion.fiefdomObj.setPersonalCastles(1);
        rebellion.fiefdomObj.setFiefdomCastles(2);
        rebellion.perform(loyalVassals);
        
        // Assert that the fiefdom's rebellious vassals are set to the number of loyal vassals minus the computed value
        int expectedRebelliousVassals = loyalVassals.size() - (1*10 + 2*25);
        Assert.assertTrue(rebellion.fiefdomObj.isRebelliousVassals());
        Assert.assertEquals(rebellion.fiefdomObj.getValue(), expectedRebelliousVassals);
    }
}