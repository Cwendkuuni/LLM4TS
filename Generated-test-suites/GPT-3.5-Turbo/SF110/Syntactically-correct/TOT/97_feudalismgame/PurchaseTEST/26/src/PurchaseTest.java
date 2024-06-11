import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class PurchaseTest {
   
    private Purchase purchase;
    
    @Before
    public void setUp() {
        purchase = new Purchase();
    }
    
    @Test
    public void testPerform() {
        // Typical use case
        Collection args1 = new ArrayList();
        
        purchase.perform(args1);
        
        // Edge case
        Collection args2 = new ArrayList();
        args2.add("vassal");
        Iterator argIter2 = args2.iterator();
        
        purchase.perform(argIter2);
        
        // Error scenario
        Collection args3 = new ArrayList();
        args3.add("invalidMethod");
        Iterator argIter3 = args3.iterator();
        
        purchase.perform(argIter3);
    }
    
    @Test
    public void testVassal() {
        // Typical use case
        Iterator arg1 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        purchase.vassal(arg1);
        
        // Edge case
        Iterator arg2 = Arrays.asList("0", "buyerName", "fiefdomName").iterator();
        
        purchase.vassal(arg2);
        
        // Error scenario
        Iterator arg3 = Arrays.asList("1", "buyerName").iterator();
        
        purchase.vassal(arg3);
    }
    
    @Test
    public void testCastleFiefdom() {
        // Typical use case
        Iterator arg1 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        purchase.castleFiefdom(arg1);
        
        // Edge case
        Iterator arg2 = Arrays.asList("0", "buyerName", "fiefdomName").iterator();
        
        purchase.castleFiefdom(arg2);
        
        // Error scenario
        Iterator arg3 = Arrays.asList("1", "buyerName").iterator();
        
        purchase.castleFiefdom(arg3);
    }
    
    @Test
    public void testCastlePersonal() {
        // Typical use case
        Iterator arg1 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        purchase.castlePersonal(arg1);
        
        // Edge case
        Iterator arg2 = Arrays.asList("0", "buyerName", "fiefdomName").iterator();
        
        purchase.castlePersonal(arg2);
        
        // Error scenario
        Iterator arg3 = Arrays.asList("1", "buyerName").iterator();
        
        purchase.castlePersonal(arg3);
    }
    
    @Test
    public void testMercenary() {
        // Typical use case
        Iterator arg1 = Arrays.asList("1", "buyerName", "knight").iterator();
        
        purchase.mercenary(arg1);
        
        // Edge case
        Iterator arg2 = Arrays.asList("0", "buyerName", "knight").iterator();
        
        purchase.mercenary(arg2);
        
        // Error scenario
        Iterator arg3 = Arrays.asList("1", "buyerName").iterator();
        
        purchase.mercenary(arg3);
    }
}