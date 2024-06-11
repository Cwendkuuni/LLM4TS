import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {
    
    @Test
    public void testPerform() {
        // Test normal functionality
        // Setup
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        // Test
        purchase.perform(args);
        
    }
    
    @Test
    public void testVassal() {
        // Test normal functionality
        // Setup
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        // Test
        purchase.vassal(arg);
        
    }
    
    @Test
    public void testCastleFiefdom() {
        // Test normal functionality
        // Setup
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        // Test
        purchase.castleFiefdom(arg);
        
    }
    
    @Test
    public void testCastlePersonal() {
        // Test normal functionality
        // Setup
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        // Test
        purchase.castlePersonal(arg);
        
    }
    
    @Test
    public void testMercenary() {
        // Test normal functionality
        // Setup
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        // Test
        purchase.mercenary(arg);
        
    }
}