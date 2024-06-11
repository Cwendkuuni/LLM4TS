import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform_typical() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        args.add("castlePersonal");
        args.add("1");
        args.add("buyerName");
        args.add("fiefdomName");
        purchase.perform(args);
        // Assert statements
    }
    
    @Test
    public void testPerform_edge() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        args.add("castleFiefdom");
        args.add("1000000");
        args.add("buyerName");
        args.add("fiefdomName");
        purchase.perform(args);
        // Assert statements
    }
    
    @Test
    public void testPerform_error() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        args.add("vassal");
        args.add("10");
        args.add("buyerName");
        args.add("fiefdomName");
        purchase.perform(args);
        // Assert statements
    }
    
    @Test
    public void testVassal_typical() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("10", "buyerName", "fiefdomName").iterator();
        purchase.vassal(arg);
        // Assert statements
    }
    
    @Test
    public void testVassal_edge() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1000000", "buyerName", "fiefdomName").iterator();
        purchase.vassal(arg);
        // Assert statements
    }
    
    @Test
    public void testVassal_error() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("10", "buyerName", "nonExistingFiefdomName").iterator();
        purchase.vassal(arg);
        // Assert statements
    }
    
    @Test
    public void testCastleFiefdom_typical() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(arg);
        // Assert statements
    }
    
    @Test
    public void testCastleFiefdom_edge() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("100000", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(arg);
        // Assert statements
    }
    
    @Test
    public void testCastleFiefdom_error() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("10", "buyerName", "nonExistingFiefdomName").iterator();
        purchase.castleFiefdom(arg);
        // Assert statements
    }
    
    @Test
    public void testCastlePersonal_typical() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(arg);
        // Assert statements
    }
    
    @Test
    public void testCastlePersonal_edge() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("10", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(arg);
        // Assert statements
    }
    
    @Test
    public void testCastlePersonal_error() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("1", "buyerName", "nonExistingFiefdomName").iterator();
        purchase.castlePersonal(arg);
        // Assert statements
    }
    
    @Test
    public void testMercenary_typical() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("10", "buyerName", "knightName").iterator();
        purchase.mercenary(arg);
        // Assert statements
    }
    
    @Test
    public void testMercenary_edge() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("100000", "buyerName", "knightName").iterator();
        purchase.mercenary(arg);
        // Assert statements
    }
    
    @Test
    public void testMercenary_error() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("10", "buyerName", "nonExistingKnightName").iterator();
        purchase.mercenary(arg);
        // Assert statements
    }

}