package src;

import org.junit.Test;
import java.util.*;

public class PurchaseTest {
    
    @Test
    public void testPerform(){
        // Create a Purchase object
        Purchase purchase = new Purchase();
        
        // Prepare test arguments
        Collection args = new ArrayList();
        args.add("vassal");
        
        // Invoke the perform method
        purchase.perform(args);
        
        // Add assertions here
    }
    
    @Test
    public void testVassal(){
        // Create a Purchase object
        Purchase purchase = new Purchase();
        
        // Prepare test arguments
        Iterator arg = Arrays.asList(10, "John", "fiefdom1").iterator();
        
        // Invoke the vassal method
        purchase.vassal(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testCastleFiefdom(){
        // Create a Purchase object
        Purchase purchase = new Purchase();
        
        // Prepare test arguments
        Iterator arg = Arrays.asList(5, "John", "fiefdom2").iterator();
        
        // Invoke the castleFiefdom method
        purchase.castleFiefdom(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testCastlePersonal(){
        // Create a Purchase object
        Purchase purchase = new Purchase();
        
        // Prepare test arguments
        Iterator arg = Arrays.asList(3, "John", "fiefdom3").iterator();
        
        // Invoke the castlePersonal method
        purchase.castlePersonal(arg);
        
        // Add assertions here
    }
    
    @Test
    public void testMercenary(){
        // Create a Purchase object
        Purchase purchase = new Purchase();
        
        // Prepare test arguments
        Iterator arg = Arrays.asList(10, "John", "Knight1").iterator();
        
        // Invoke the mercenary method
        purchase.mercenary(arg);
        
        // Add assertions here
    }
}