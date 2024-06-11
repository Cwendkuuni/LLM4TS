import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Test case for perform method
        // Typical use case
        Collection<String> args = new ArrayList<>();
        args.add("vassal");
        args.add("2");
        args.add("John");
        args.add("fiefdom1");
        Purchase purchase = new Purchase();
        purchase.perform(args);

        // Edge case
        // args is null
        args = null;
        purchase.perform(args);

        // Error scenario
        // args is empty
        args = new ArrayList<>();
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        // Test case for vassal method
        // Typical use case
        Iterator<String> arg = Arrays.asList("2", "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.vassal(arg);

        // Edge case
        // qty is negative
        arg = Arrays.asList("-2", "John", "fiefdom1").iterator();
        purchase.vassal(arg);

        // Error scenario
        // fiefdom doesn't belong to the kingdom
        arg = Arrays.asList("2", "John", "nonexistentFiefdom").iterator();
        purchase.vassal(arg);
    }

    @Test
    public void testCastleFiefdom() {
        // Test case for castleFiefdom method
        // Typical use case
        Iterator<String> arg = Arrays.asList("2", "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.castleFiefdom(arg);

        // Edge case
        // qty is zero
        arg = Arrays.asList("0", "John", "fiefdom1").iterator();
        purchase.castleFiefdom(arg);

        // Error scenario
        // fiefdom belongs to another kingdom
        arg = Arrays.asList("2", "John", "fiefdom2").iterator();
        purchase.castleFiefdom(arg);
    }

    @Test
    public void testCastlePersonal() {
        // Test case for castlePersonal method
        // Typical use case
        Iterator<String> arg = Arrays.asList("2", "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.castlePersonal(arg);

        // Edge case
        // qty is positive
        arg = Arrays.asList("-2", "John", "fiefdom1").iterator();
        purchase.castlePersonal(arg);

        // Error scenario
        // fiefdom doesn't belong to the kingdom
        arg = Arrays.asList("2", "John", "nonexistentFiefdom").iterator();
        purchase.castlePersonal(arg);
    }

    @Test
    public void testMercenary() {
        // Test case for mercenary method
        // Typical use case
        Iterator<String> arg = Arrays.asList("2", "John", "Knight1").iterator();
        Purchase purchase = new Purchase();
        purchase.mercenary(arg);

        // Edge case
        // qty is zero
        arg = Arrays.asList("0", "John", "Knight1").iterator();
        purchase.mercenary(arg);

        // Error scenario
        // buyer knight doesn't exist
        arg = Arrays.asList("2", "John", "nonexistentKnight").iterator();
        purchase.mercenary(arg);
    }
}