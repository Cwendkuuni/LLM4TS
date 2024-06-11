import org.junit.Test;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection args = new ArrayList();
        purchase.perform(args);
        // assert statements
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator arg = new ArrayList().iterator();
        purchase.vassal(arg);
        // assert statements
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator arg = new ArrayList().iterator();
        purchase.castleFiefdom(arg);
        // assert statements
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator arg = new ArrayList().iterator();
        purchase.castlePersonal(arg);
        // assert statements
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator arg = new ArrayList().iterator();
        purchase.mercenary(arg);
        // assert statements
    }
}