import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
    
    VassalRebellion vr;
    
    @Before
    public void setUp() {
        vr = new VassalRebellion();
    }
    
    @After
    public void tearDown() {
        vr = null;
    }
    
    @Test
    public void testPerform_ValidInput_RebelliousVassalsSet() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        
        // Act
        vr.perform(col);
        
        // Assert
        assertTrue(vr.fiefdomObj.isRebelliousVassals());
    }
    
    @Test
    public void testPerform_NoLoyalVassals_RollEventCalled() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom2");
        
        // Act
        vr.perform(col);
        
        // Assert
        // Assertion to check if RollEvent is called can be added here
    }
    
    @Test
    public void testPerform_PersonalCastlesAndFiefdomCastlesGreaterRebelliousVassalsSet() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom3");
        vr.fiefdomObj = new Fiefdoms();
        vr.fiefdomObj.setPersonalCastles(2);
        vr.fiefdomObj.setFiefdomCastles(3);
        vr.fiefdomObj.setLoyalVassals(new ArrayList<>());
        vr.fiefdomObj.getLoyalVassals().add("Vassal1");
        vr.fiefdomObj.getLoyalVassals().add("Vassal2");
        vr.fiefdomObj.getLoyalVassals().add("Vassal3");
        
        // Act
        vr.perform(col);
        
        // Assert
        assertTrue(vr.fiefdomObj.isRebelliousVassals());
    }
    
    @Test
    public void testPerform_LoyalVassalsLessThanValue_RebelliousVassalsSet() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom4");
        vr.fiefdomObj = new Fiefdoms();
        vr.fiefdomObj.setValue(10);
        vr.fiefdomObj.setLoyalVassals(new ArrayList<>());
        vr.fiefdomObj.getLoyalVassals().add("Vassal1");
        vr.fiefdomObj.getLoyalVassals().add("Vassal2");
        
        // Act
        vr.perform(col);
        
        // Assert
        assertTrue(vr.fiefdomObj.isRebelliousVassals());
    }
    
    @Test
    public void testPerform_PersonalAndFiefdomCastlesZero_RebelliousVassalsNotSet() {
        // Arrange
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom5");
        vr.fiefdomObj = new Fiefdoms();
        vr.fiefdomObj.setPersonalCastles(0);
        vr.fiefdomObj.setFiefdomCastles(0);
        vr.fiefdomObj.setLoyalVassals(new ArrayList<>());
        vr.fiefdomObj.getLoyalVassals().add("Vassal1");
        vr.fiefdomObj.getLoyalVassals().add("Vassal2");
        
        // Act
        vr.perform(col);
        
        // Assert
        assertFalse(vr.fiefdomObj.isRebelliousVassals());
    }
}