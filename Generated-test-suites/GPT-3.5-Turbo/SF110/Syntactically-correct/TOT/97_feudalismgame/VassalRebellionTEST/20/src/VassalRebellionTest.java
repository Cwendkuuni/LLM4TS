import org.junit.jupiter.api.*;
import java.util.*;

class VassalRebellionTest {
    
    private VassalRebellion vassalRebellion;
    
    @BeforeEach
    void setUp() {
        vassalRebellion = new VassalRebellion();
    }
    
    @Test
    void testPerform_withLoyalVassalsAndPositiveValue_setRebelliousVassalsAndSetKingdomFalse() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        Map.map.put("Fiefdom1", new Fiefdoms());
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        Assertions.assertTrue(Map.map.get("Fiefdom1").isRebelliousVassals());
        Assertions.assertFalse(Map.map.get("Fiefdom1").isKingdom());
    }
    
    @Test
    void testPerform_withLoyalVassalsAndNegativeValue_setRebelliousVassalsBasedOnLoyalVassalsSize() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom2");
        Map.map.put("Fiefdom2", new Fiefdoms());
        Map.map.get("Fiefdom2").setLoyalVassals(new ArrayList<>());
        Map.map.get("Fiefdom2").setValue(5);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        Assertions.assertTrue(Map.map.get("Fiefdom2").isRebelliousVassals());
        Assertions.assertEquals(0, Map.map.get("Fiefdom2").getRebelliousVassals());
    }
    
    @Test
    void testPerform_withLoyalVassalsAndPositivePersonalCastlesAndNegativeValue_setRebelliousVassalsBasedOnPersonalCastlesAndFiefdomCastles() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom3");
        Map.map.put("Fiefdom3", new Fiefdoms());
        Map.map.get("Fiefdom3").setLoyalVassals(new ArrayList<>());
        Map.map.get("Fiefdom3").setPersonalCastles(2);
        Map.map.get("Fiefdom3").setFiefdomCastles(3);
        Map.map.get("Fiefdom3").setValue(4);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        Assertions.assertTrue(Map.map.get("Fiefdom3").isRebelliousVassals());
        Assertions.assertEquals(16, Map.map.get("Fiefdom3").getRebelliousVassals());
    }
    
    @Test
    void testPerform_withNoLoyalVassals_createRollEvent() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom4");
        Map.map.put("Fiefdom4", new Fiefdoms());
        Map.map.get("Fiefdom4").setLoyalVassals(new ArrayList<>());
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        Assertions.assertEquals(1, RollEvent.getRollEvents().size());
        Assertions.assertEquals("Fiefdom4", RollEvent.getRollEvents().get(0).getFiefdomName());
    }
}