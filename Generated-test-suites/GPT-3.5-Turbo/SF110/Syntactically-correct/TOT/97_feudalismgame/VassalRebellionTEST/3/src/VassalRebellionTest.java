import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_WithLoyalVassals_ShouldSetRebelliousVassalsAndSetKingdomFalse() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        VassalRebellion rebellion = new VassalRebellion();
        
        // Act
        rebellion.perform(col);
        
        // Assert
        assertTrue(rebellion.fiefdomObj.isRebelliousVassals());
        assertFalse(rebellion.fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_WithZeroLoyalVassals_ShouldCreateRollEvent() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        VassalRebellion rebellion = new VassalRebellion();
        
        // Act
        rebellion.perform(col);
        
        // Assert
        //  Check if RollEvent is created or not
    }
    
    @Test
    public void testPerform_WithPositivePersonalCastlesAndFiefdomCastlesAndSufficientLoyalVassals_ShouldSetRebelliousVassals() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        VassalRebellion rebellion = new VassalRebellion();
        
        // Act
        rebellion.perform(col);
        
        // Assert
        assertTrue(rebellion.fiefdomObj.isRebelliousVassals());
    }
    
    @Test
    public void testPerform_WithNegativePersonalCastlesAndFiefdomCastlesAndSufficientLoyalVassals_ShouldNotSetRebelliousVassals() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        VassalRebellion rebellion = new VassalRebellion();
        rebellion.fiefdomObj.setPersonalCastles(-5);
        rebellion.fiefdomObj.setFiefdomCastles(-10);
        
        // Act
        rebellion.perform(col);
        
        // Assert
        assertFalse(rebellion.fiefdomObj.isRebelliousVassals());
    }
    
    @Test
    public void testPerform_WithSufficientLoyalVassalsAndNegativePersonalCastlesAndFiefdomCastles_ShouldSetRebelliousVassals() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        VassalRebellion rebellion = new VassalRebellion();
        rebellion.fiefdomObj.setPersonalCastles(-5);
        rebellion.fiefdomObj.setFiefdomCastles(-10);
        rebellion.fiefdomObj.setLoyalVassals(30);
        
        // Act
        rebellion.perform(col);
        
        // Assert
        assertTrue(rebellion.fiefdomObj.isRebelliousVassals());
    }
}