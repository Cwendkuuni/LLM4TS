import org.junit.*;
import java.util.*;
import java.lang.reflect.*;

public class PurchaseTest {
    
    Purchase purchase;
    
    @Before
    public void setUp() {
        purchase = new Purchase();
    }
    
    @Test
    public void testPerform() {
        // Typical use case
        Collection args = new ArrayList();
        args.add("vassal");
        args.add(5);
        args.add("buyer");
        args.add("fiefdom");
        
        purchase.perform(args);
        
        // Edge case
        args.clear();
        args.add("mercenary");
        args.add(3);
        args.add("buyer");
        args.add("knight");
        
        purchase.perform(args);
        
        // Error scenario
        args.clear();
        args.add("invalidMethod");
        
        purchase.perform(args);
    }
    
    @Test
    public void testVassal() {
        // Typical use case
        Iterator arg = Arrays.asList(5, "buyer", "fiefdom").iterator();
        purchase.vassal(arg);
        
        // Edge case
        arg = Arrays.asList(0, "buyer", "fiefdom").iterator();
        purchase.vassal(arg);
        
        // Error scenario
        arg = Arrays.asList(5, "nonexistentBuyer", "fiefdom").iterator();
        purchase.vassal(arg);
    }
    
    @Test
    public void testCastleFiefdom() {
        // Typical use case
        Iterator arg = Arrays.asList(2, "buyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);
        
        // Edge case
        arg = Arrays.asList(0, "buyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);
        
        // Error scenario
        arg = Arrays.asList(2, "nonexistentBuyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);
    }
    
    @Test
    public void testCastlePersonal() {
        // Typical use case
        Iterator arg = Arrays.asList(3, "buyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);
        
        // Edge case
        arg = Arrays.asList(0, "buyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);
        
        // Error scenario
        arg = Arrays.asList(3, "nonexistentBuyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);
    }
    
    @Test
    public void testMercenary() {
        // Typical use case
        Iterator arg = Arrays.asList(4, "buyer", "knight").iterator();
        purchase.mercenary(arg);
        
        // Edge case
        arg = Arrays.asList(0, "buyer", "knight").iterator();
        purchase.mercenary(arg);
        
        // Error scenario
        arg = Arrays.asList(4, "nonexistentBuyer", "knight").iterator();
        purchase.mercenary(arg);
    }
}