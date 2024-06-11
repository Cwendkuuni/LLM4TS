
package src;

import org.junit.Test;

import java.util.*;

public class PurchaseTest {
    
    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        args.add("vassal");
        purchase.perform(args);
        
        args.clear();
        args.add("castleFiefdom");
        purchase.perform(args);
        
        args.clear();
        args.add("castlePersonal");
        purchase.perform(args);
        
        args.clear();
        args.add("mercenary");
        purchase.perform(args);
    }
    
    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.vassal(arg);
    }
    
    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.castleFiefdom(arg);
    }
    
    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.castlePersonal(arg);
    }
    
    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.mercenary(arg);
    }
}
