package src;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PurchaseTest {
    
    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        args.add("vassal");
        purchase.perform(args);
        
        // Add assertions here
    }
    
    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        purchase.vassal(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1", "buyerName", "knightName").iterator();
        purchase.mercenary(arg);
        
        // Add assertions here
    }
}

