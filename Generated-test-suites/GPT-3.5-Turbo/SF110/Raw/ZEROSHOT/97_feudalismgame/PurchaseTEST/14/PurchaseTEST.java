import org.junit.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection args = new ArrayList();
        purchase.perform(args);
        // Add assertions here
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator arg = new Iterator();
        purchase.vassal(arg);
        // Add assertions here
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator arg = new Iterator();
        purchase.castleFiefdom(arg);
        // Add assertions here
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator arg = new Iterator();
        purchase.castlePersonal(arg);
        // Add assertions here
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator arg = new Iterator();
        purchase.mercenary(arg);
        // Add assertions here
    }
}