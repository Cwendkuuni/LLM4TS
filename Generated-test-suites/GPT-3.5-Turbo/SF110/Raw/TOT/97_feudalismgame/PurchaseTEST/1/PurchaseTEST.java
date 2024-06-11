import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Typical use case
        Purchase purchase = new Purchase();
        purchase.perform(someCollection);
        // Assert something
    }
    
    @Test
    public void testVassal() {
        // Typical use case
        Purchase purchase = new Purchase();
        Iterator arg = someIterator;
        purchase.vassal(arg);
        // Assert something
        
        // Edge case: buyerObj is a king and has already bought all vassals
        // Modify the buyerObj
        purchase.vassal(arg);
        // Assert something
        
        // Edge case: buyerObj is a king but fiefdom has another owner
        // Modify the buyerObj and fiefdom
        purchase.vassal(arg);
        // Assert something
        
        // Error scenario: fiefdom doesn't belong to the kingdom
        // Modify the fiefdom
        purchase.vassal(arg);
        // Assert something
    }
    
    @Test
    public void testCastleFiefdom() {
        // Typical use case
        Purchase purchase = new Purchase();
        Iterator arg = someIterator;
        purchase.castleFiefdom(arg);
        // Assert something
        
        // Edge case: buyerObj is a king and has already bought all fiefdom castles
        // Modify the buyerObj and fiefdom
        purchase.castleFiefdom(arg);
        // Assert something
        
        // Edge case: buyerObj is a king but fiefdom has another owner or contains vikings/magyar/muslims
        // Modify the buyerObj and fiefdom
        purchase.castleFiefdom(arg);
        // Assert something
        
        // Error scenario: fiefdom doesn't belong to the kingdom
        // Modify the fiefdom
        purchase.castleFiefdom(arg);
        // Assert something
    }
    
    @Test
    public void testCastlePersonal() {
        // Typical use case
        Purchase purchase = new Purchase();
        Iterator arg = someIterator;
        purchase.castlePersonal(arg);
        // Assert something
        
        // Edge case: buyerObj is a king and has already bought all personal castles
        // Modify the buyerObj and fiefdom
        purchase.castlePersonal(arg);
        // Assert something
        
        // Edge case: buyerObj is a king but fiefdom has another owner or contains vikings/magyar/muslims
        // Modify the buyerObj and fiefdom
        purchase.castlePersonal(arg);
        // Assert something
        
        // Edge case: buyerObj is a count/duke
        // Modify the buyerObj
        purchase.castlePersonal(arg);
        // Assert something
        
        // Error scenario: fiefdom doesn't belong to the kingdom
        // Modify the fiefdom
        purchase.castlePersonal(arg);
        // Assert something
    }
    
    @Test
    public void testMercenary() {
        // Typical use case
        Purchase purchase = new Purchase();
        Iterator arg = someIterator;
        purchase.mercenary(arg);
        // Assert something
        
        // Edge case: buyerObj is a king and has already bought all mercenaries
        // Modify the buyerObj and knight
        purchase.mercenary(arg);
        // Assert something
        
        // Edge case: buyerObj is not the same as knight
        // Modify the buyerObj and knight
        purchase.mercenary(arg);
        // Assert something
        
        // Error scenario: buyerObj or knight doesn't exist
        // Modify the buyerObj and knight
        purchase.mercenary(arg);
        // Assert something
    }
}