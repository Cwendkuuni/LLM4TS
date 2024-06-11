import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void vassalTest() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.vassal(arg);
        // write assertions here
    }

    @Test
    public void castleFiefdomTest() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("3", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(arg);
        // write assertions here
    }

    @Test
    public void castlePersonalTest() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("2", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(arg);
        // write assertions here
    }

    @Test
    public void mercenaryTest() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("4", "buyerName", "knightName").iterator();
        purchase.mercenary(arg);
        // write assertions here
    }

}