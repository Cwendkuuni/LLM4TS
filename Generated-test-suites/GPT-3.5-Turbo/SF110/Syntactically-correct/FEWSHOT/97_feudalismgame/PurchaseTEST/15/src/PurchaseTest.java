import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<String>();
        args.add("vassal");
        
        try {
            purchase.perform(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator<String> args = new ArrayList<String>().iterator();
        args.add("1");
        args.add("buyerName");
        args.add("fiefdom");
        
        try {
            purchase.vassal(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator<String> args = new ArrayList<String>().iterator();
        args.add("1");
        args.add("buyerName");
        args.add("fiefdom");
        
        try {
            purchase.castleFiefdom(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator<String> args = new ArrayList<String>().iterator();
        args.add("1");
        args.add("buyerName");
        args.add("fiefdom");
        
        try {
            purchase.castlePersonal(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator<String> args = new ArrayList<String>().iterator();
        args.add("1");
        args.add("buyerName");
        args.add("knight");
        
        try {
            purchase.mercenary(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}