import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        // Test when fiefdom has an owner and the buyer is a king or owns the fiefdom
        Iterator<String> args = Arrays.asList("3", "John", "fiefdomA").iterator();
        purchase.vassal(args);
        // assertions here
        
        // Test when fiefdom has an owner and the buyer is a count or a duke
        args = Arrays.asList("3", "Bob", "fiefdomA").iterator();
        purchase.vassal(args);
        // assertions here
        
        // Test when fiefdom doesn't belong to the kingdom
        args = Arrays.asList("3", "John", "fiefdomB").iterator();
        purchase.vassal(args);
        // assertions here
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        // Test when fiefdom has an owner and the buyer is a king or owns the fiefdom and has no vikings, magyar, or muslim
        Iterator<String> args = Arrays.asList("2", "John", "fiefdomA").iterator();
        purchase.castleFiefdom(args);
        // assertions here
        
        // Test when fiefdom has an owner and the buyer is a count or a duke
        args = Arrays.asList("2", "Bob", "fiefdomA").iterator();
        purchase.castleFiefdom(args);
        // assertions here
        
        // Test when fiefdom doesn't belong to the kingdom
        args = Arrays.asList("2", "John", "fiefdomB").iterator();
        purchase.castleFiefdom(args);
        // assertions here
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        // Test when fiefdom has an owner and the buyer is a king and the fiefdom owner is a knight
        Iterator<String> args = Arrays.asList("4", "John", "fiefdomA").iterator();
        purchase.castlePersonal(args);
        // assertions here
        
        // Test when fiefdom has an owner and the buyer is a king and the fiefdom owner is not a knight
        args = Arrays.asList("4", "John", "fiefdomB").iterator();
        purchase.castlePersonal(args);
        // assertions here
        
        // Test when fiefdom has an owner and the buyer is a count or a duke
        args = Arrays.asList("4", "Bob", "fiefdomA").iterator();
        purchase.castlePersonal(args);
        // assertions here
        
        // Test when fiefdom doesn't belong to the kingdom
        args = Arrays.asList("4", "John", "fiefdomC").iterator();
        purchase.castlePersonal(args);
        // assertions here
    }
    
    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        // Test when both buyer and knight exist, and the buyer is the knight himself or a king
        Iterator<String> args = Arrays.asList("5", "John", "John").iterator();
        purchase.mercenary(args);
        // assertions here
        
        // Test when both buyer and knight exist, and the buyer is not the knight and not a king
        args = Arrays.asList("5", "Bob", "John").iterator();
        purchase.mercenary(args);
        // assertions here
        
        // Test when either buyer or knight doesn't exist
        args = Arrays.asList("5", "Alice", "John").iterator();
        purchase.mercenary(args);
        // assertions here
    }
}