import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_WithLoyalVassalsAndEnoughRebelliousVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdom = new Fiefdoms();
        fiefdom.setName("Fiefdom1");
        fiefdom.setLoyalVassals(new ArrayList<Vassal>());
        fiefdom.setValue(0);
        fiefdom.setPersonalCastles(2);
        fiefdom.setFiefdomCastles(3);
        map.put(fiefdom.getName(), fiefdom);
        Map.setMap(map);
        Collection<String> col = new ArrayList<String>();
        col.add(fiefdom.getName());
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        Assertions.assertTrue(fiefdom.isRebelliousVassalsSet());
        Assertions.assertFalse(fiefdom.isKingdom());
        Assertions.assertEquals(0, fiefdom.getLoyalVassals().size());
    }
    
    @Test
    public void testPerform_WithLoyalVassalsAndNotEnoughRebelliousVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdom = new Fiefdoms();
        fiefdom.setName("Fiefdom2");
        fiefdom.setLoyalVassals(new ArrayList<Vassal>());
        fiefdom.setValue(5);
        fiefdom.setPersonalCastles(0);
        fiefdom.setFiefdomCastles(0);
        map.put(fiefdom.getName(), fiefdom);
        Map.setMap(map);
        Collection<String> col = new ArrayList<String>();
        col.add(fiefdom.getName());
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        Assertions.assertFalse(fiefdom.isRebelliousVassalsSet());
        Assertions.assertTrue(fiefdom.isKingdom());
        Assertions.assertEquals(5, fiefdom.getRebelliousVassals().size());
    }
    
    @Test
    public void testPerform_WithNoLoyalVassals() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        Map<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
        Fiefdoms fiefdom = new Fiefdoms();
        fiefdom.setName("Fiefdom3");
        fiefdom.setLoyalVassals(new ArrayList<Vassal>());
        map.put(fiefdom.getName(), fiefdom);
        Map.setMap(map);
        Collection<String> col = new ArrayList<String>();
        col.add(fiefdom.getName());
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        // Verify that a RollEvent is created
        // (Unable to assert specific behavior without additional code)
    }
}