import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Test perform method
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<String>();
        args.add("vassal");
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        // Test vassal method
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.vassal(arg);
    }

    @Test
    public void testCastleFiefdom() {
        // Test castleFiefdom method
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.castleFiefdom(arg);
    }

    @Test
    public void testCastlePersonal() {
        // Test castlePersonal method
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.castlePersonal(arg);
    }

    @Test
    public void testMercenary() {
        // Test mercenary method
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        purchase.mercenary(arg);
    }

}