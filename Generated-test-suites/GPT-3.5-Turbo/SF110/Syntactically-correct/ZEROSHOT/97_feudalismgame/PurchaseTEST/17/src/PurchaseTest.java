import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Collection<String> args = new ArrayList<>();
        Purchase purchase = new Purchase();
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        Iterator<String> arg = new ArrayList<String>().iterator();
        Purchase purchase = new Purchase();
        purchase.vassal(arg);
        // Add assertions here
    }

    @Test
    public void testCastleFiefdom() {
        Iterator<String> arg = new ArrayList<String>().iterator();
        Purchase purchase = new Purchase();
        purchase.castleFiefdom(arg);
        // Add assertions here
    }

    @Test
    public void testCastlePersonal() {
        Iterator<String> arg = new ArrayList<String>().iterator();
        Purchase purchase = new Purchase();
        purchase.castlePersonal(arg);
        // Add assertions here
    }

    @Test
    public void testMercenary() {
        Iterator<String> arg = new ArrayList<String>().iterator();
        Purchase purchase = new Purchase();
        purchase.mercenary(arg);
        // Add assertions here
    }
}