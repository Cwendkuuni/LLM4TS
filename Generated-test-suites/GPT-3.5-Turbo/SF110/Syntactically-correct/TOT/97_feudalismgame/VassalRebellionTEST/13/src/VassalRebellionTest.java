import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_NoLoyalVassals() {
        // Test with no loyal vassals
        Collection<String> col = new ArrayList<>();
        col.add("FiefdomA");

        vassalRebellion.perform(col);

        // Assert that a new RollEvent is created for the fiefdom with no loyal vassals
        // This would ensure proper handling when there are no loyal vassals
    }
    
    @Test
    public void testPerform_LoyalVassalsNoRebellion() {
        // Test with loyal vassals but no rebellion
        Collection<String> col = new ArrayList<>();
        col.add("FiefdomA");
        
        // Add loyal vassals to the fiefdom
        Fiefdoms fiefdomA = new Fiefdoms();
        fiefdomA.setLoyalVassals(Collections.singletonList("Vassal1"));
        Map.map.put("FiefdomA", fiefdomA);

        vassalRebellion.perform(col);

        // Assert that no rebellious vassals are set for the fiefdom
        // This would ensure correct handling when there are loyal vassals but no rebellion
        Assert.assertEquals(0, fiefdomA.getRebelliousVassals().size());
    }
    
    @Test
    public void testPerform_LoyalVassalsWithRebellion() {
        // Test with loyal vassals and rebellion
        Collection<String> col = new ArrayList<>();
        col.add("FiefdomA");
        
        // Add loyal vassals to the fiefdom more than its value, so rebellion occurs
        Fiefdoms fiefdomA = new Fiefdoms();
        fiefdomA.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        fiefdomA.setValue(1); // Setting a value less than the number of loyal vassals
        Map.map.put("FiefdomA", fiefdomA);
        
        vassalRebellion.perform(col);
        
        // Assert that rebellious vassals are set and kingdom is set to false
        // This would ensure correct handling when there are loyal vassals and rebellion occurs
        Assert.assertEquals(1, fiefdomA.getRebelliousVassals().size());
        Assert.assertFalse(fiefdomA.isKingdom());
    }
    
    @Test
    public void testPerform_LoyalVassalsWithCastleRebellion() {
        // Test with loyal vassals and rebellion based on castle count
        Collection<String> col = new ArrayList<>();
        col.add("FiefdomA");
        
        // Add loyal vassals to the fiefdom and set castle counts
        Fiefdoms fiefdomA = new Fiefdoms();
        fiefdomA.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        fiefdomA.setPersonalCastles(1); // Setting personal castles for the fiefdom
        fiefdomA.setFiefdomCastles(2); // Setting fiefdom castles for the fiefdom
        Map.map.put("FiefdomA", fiefdomA);
        
        vassalRebellion.perform(col);
        
        // Assert that rebellious vassals are set based on castle count
        // This would ensure correct handling when there are loyal vassals and rebellion occurs based on castle count
        Assert.assertEquals(1, fiefdomA.getRebelliousVassals().size());
    }

}