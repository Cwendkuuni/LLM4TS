
import org.junit.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        List<String> args = new ArrayList<>();
        purchase.perform(args);
        // Assert statements
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        List<String> arg = new ArrayList<>();
        purchase.vassal(arg.iterator());
        // Assert statements
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        List<String> arg = new ArrayList<>();
        purchase.castleFiefdom(arg.iterator());
        // Assert statements
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        List<String> arg = new ArrayList<>();
        purchase.castlePersonal(arg.iterator());
        // Assert statements
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        List<String> arg = new ArrayList<>();
        purchase.mercenary(arg.iterator());
        // Assert statements
    }

}
