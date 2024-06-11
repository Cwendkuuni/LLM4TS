package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {
    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.vassal(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyerName", "knightName").iterator();
        purchase.mercenary(arg);
        
        // Add assertions here
    }
}