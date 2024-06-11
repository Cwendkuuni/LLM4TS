package src;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_whenLoyalVassalsExist_shouldSetRebelliousVassalsAndSetKingdomFalse() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        String fiefdomName = "TestFiefdom";
        
        // Act
        vr.perform(Arrays.asList(fiefdomName));
        
        // Assert
        Fiefdoms fiefdomObj = vr.getFiefdomObj();
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_whenLoyalVassalsDoesNotExist_shouldCreateRollEvent() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        String fiefdomName = "TestFiefdom";
        Map.map.clear(); // Clear map to simulate no loyal vassals
        
        // Act
        vr.perform(Arrays.asList(fiefdomName));
        
        // Assert
        // Check if RollEvent is created
        // ...
    }
    
    @Test
    public void testPerform_whenLoyalVassalsExistAndRebelliousVassalsMoreThanLoyalVassals_shouldSetRebelliousVassalsEqualToLoyalVassals() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        String fiefdomName = "TestFiefdom";
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        fiefdomObj.setValue(3); // Set loyal vassals count more than available
        Map.map.put(fiefdomName, fiefdomObj);
        
        // Act
        vr.perform(Arrays.asList(fiefdomName));
        
        // Assert
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(fiefdomObj.getLoyalVassals().size(), fiefdomObj.getRebelliousVassals().size());
    }
    
    @Test
    public void testPerform_whenPersonalCastlesAndFiefdomCastlesZeroAndLoyalVassalsMoreThanRebelliousVassals_shouldSetRebelliousVassalsEqualToDifference() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        String fiefdomName = "TestFiefdom";
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        fiefdomObj.setValue(1);
        Map.map.put(fiefdomName, fiefdomObj);
        
        // Act
        vr.perform(Arrays.asList(fiefdomName));
        
        // Assert
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(fiefdomObj.getLoyalVassals().size() - fiefdomObj.getValue(), fiefdomObj.getRebelliousVassals().size());
    }
    
    @Test
    public void testPerform_whenPersonalCastlesAndFiefdomCastlesMoreThanZeroAndLoyalVassalsMoreThanRebelliousVassals_shouldSetRebelliousVassalsEqualToDifference() {
        // Arrange
        VassalRebellion vr = new VassalRebellion();
        String fiefdomName = "TestFiefdom";
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        fiefdomObj.setValue(1);
        Map.map.put(fiefdomName, fiefdomObj);
        
        // Act
        vr.perform(Arrays.asList(fiefdomName));
        
        // Assert
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertEquals(fiefdomObj.getLoyalVassals().size() - (fiefdomObj.getPersonalCastles() * 10 + fiefdomObj.getFiefdomCastles() * 25), fiefdomObj.getRebelliousVassals().size());
    }
}