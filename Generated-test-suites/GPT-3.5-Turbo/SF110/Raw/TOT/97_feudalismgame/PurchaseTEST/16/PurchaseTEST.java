import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection args = new ArrayList<>();
        args.add("vassal");
        args.add(5);
        args.add("buyer");
        args.add("fiefdomName");
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator args = new ArrayList<>().iterator();
        args.next();
        args.next();
        args.next();
        purchase.vassal(args);
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator args = new ArrayList<>().iterator();
        args.next();
        args.next();
        args.next();
        purchase.castleFiefdom(args);
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator args = new ArrayList<>().iterator();
        args.next();
        args.next();
        args.next();
        purchase.castlePersonal(args);
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator args = new ArrayList<>().iterator();
        args.next();
        args.next();
        args.next();
        purchase.mercenary(args);
    }
}