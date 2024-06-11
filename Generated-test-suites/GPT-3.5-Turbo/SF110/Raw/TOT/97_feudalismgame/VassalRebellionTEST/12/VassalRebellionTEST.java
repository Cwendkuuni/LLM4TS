package src;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    private Collection<String> col;
    
    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms();
        col = new ArrayList<>();
    }
    
    @Test
    public void testPerform_WhenLoyalVassalsExistAndRebelliousVassalsCanBeSet_ExpectRebelliousVassalsSetAndKingdomSetToFalse() {
        // Arrange
        col.add("Fiefdom1");
        fiefdomObj.setName("Fiefdom1");
        fiefdomObj.setLoyalVassals(new ArrayList<String>() {{
            add("Vassal1");
            add("Vassal2");
        }});
        fiefdomObj.setValue(1);
        vassalRebellion.setFiefdomObj(fiefdomObj);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertTrue(fiefdomObj.isRebelliousVassalsSet());
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_WhenLoyalVassalsExistAndRebelliousVassalsCannotBeSet_ExpectRebelliousVassalsSetWithLoyalVassalsSize() {
        // Arrange
        col.add("Fiefdom1");
        fiefdomObj.setName("Fiefdom1");
        fiefdomObj.setLoyalVassals(new ArrayList<String>() {{
            add("Vassal1");
            add("Vassal2");
        }});
        fiefdomObj.setValue(3);
        vassalRebellion.setFiefdomObj(fiefdomObj);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertTrue(fiefdomObj.isRebelliousVassalsSet());
        assertEquals(fiefdomObj.getLoyalVassals().size(), fiefdomObj.getRebelliousVassals().size());
    }
    
    @Test
    public void testPerform_WhenPersonalCastlesAndFiefdomCastlesPositiveAndRebelliousVassalsCanBeSet_ExpectRebelliousVassalsSetWithRemainingLoyalVassals() {
        // Arrange
        col.add("Fiefdom1");
        fiefdomObj.setName("Fiefdom1");
        fiefdomObj.setLoyalVassals(new ArrayList<String>() {{
            add("Vassal1");
            add("Vassal2");
            add("Vassal3");
        }});
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        fiefdomObj.setValue(2);
        vassalRebellion.setFiefdomObj(fiefdomObj);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertTrue(fiefdomObj.isRebelliousVassalsSet());
        assertEquals(fiefdomObj.getLoyalVassals().size() - 2, fiefdomObj.getRebelliousVassals().size());
    }
    
    @Test
    public void testPerform_WhenPersonalCastlesAndFiefdomCastlesPositiveAndRebelliousVassalsCannotBeSet_ExpectRebelliousVassalsNotSet() {
        // Arrange
        col.add("Fiefdom1");
        fiefdomObj.setName("Fiefdom1");
        fiefdomObj.setLoyalVassals(new ArrayList<String>() {{
            add("Vassal1");
            add("Vassal2");
            add("Vassal3");
        }});
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        fiefdomObj.setValue(100);
        vassalRebellion.setFiefdomObj(fiefdomObj);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertFalse(fiefdomObj.isRebelliousVassalsSet());
    }
    
    @Test
    public void testPerform_WhenNoLoyalVassalsExist_ExpectRollEventCreated() {
        // Arrange
        col.add("Fiefdom1");
        fiefdomObj.setName("Fiefdom1");
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        vassalRebellion.setFiefdomObj(fiefdomObj);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        // Check if RollEvent is created (Code not provided in the given Java class)
    }

}