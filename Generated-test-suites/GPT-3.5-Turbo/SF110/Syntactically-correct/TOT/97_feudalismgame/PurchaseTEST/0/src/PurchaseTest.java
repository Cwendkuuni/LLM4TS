import org.junit.*;
import java.util.*;

public class PurchaseTest {

    private Purchase purchase;

    @Before
    public void setUp() {
        purchase = new Purchase();
    }

    @Test
    public void testPerform() {
        // Typical use case
        Collection<String> args = Arrays.asList("vassal", "10", "buyer", "fiefdom");
        purchase.perform(args);

        // Edge case: empty args collection
        args = new ArrayList<>();
        purchase.perform(args);

        // Error scenario: args contains invalid method name
        args = Arrays.asList("invalidMethod", "10", "buyer", "fiefdom");
        purchase.perform(args);
        // Assert that proper exception is thrown and caught

        // Error scenario: args contains null values
        args = Arrays.asList(null, null, null, null);
        purchase.perform(args);
        // Assert that IllegalArgumentException is thrown and caught

        // Error scenario: SecurityException occurs
        args = Arrays.asList("vassal", "10", "buyer", "fiefdom");
        purchase.perform(args);
        // Assert that SecurityException is thrown and caught
    }

    @Test
    public void testVassal() {
        // Typical use case
        Iterator<String> arg = Arrays.asList("10", "buyer", "fiefdom").iterator();
        purchase.vassal(arg);

        // Edge case: qty is 0
        arg = Arrays.asList("0", "buyer", "fiefdom").iterator();
        purchase.vassal(arg);

        // Error scenario: fiefdom does not belong to the kingdom
        arg = Arrays.asList("10", "buyer", "nonexistentFiefdom").iterator();
        purchase.vassal(arg);
        // Assert that "Fiefdom doesn't belong to the kingdom" message is printed

        // Error scenario: buyer is not king or does not own the fiefdom
        arg = Arrays.asList("10", "nonexistentBuyer", "fiefdom").iterator();
        purchase.vassal(arg);
        // Assert that "You can only buy a castle for a fiefdom that belongs to you" message is printed
    }

    @Test
    public void testCastleFiefdom() {
        // Typical use case
        Iterator<String> arg = Arrays.asList("10", "buyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);

        // Edge case: qty is 0
        arg = Arrays.asList("0", "buyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);

        // Error scenario: fiefdom does not belong to the kingdom
        arg = Arrays.asList("10", "buyer", "nonexistentFiefdom").iterator();
        purchase.castleFiefdom(arg);
        // Assert that "Fiefdom doesn't belong to the kingdom" message is printed

        // Error scenario: buyer is not king or does not own the fiefdom or fiefdom has troops
        arg = Arrays.asList("10", "nonexistentBuyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);
        // Assert that "You can only buy a castle for a fiefdom that belongs to you" message is printed
    }

    @Test
    public void testCastlePersonal() {
        // Typical use case
        Iterator<String> arg = Arrays.asList("10", "buyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);

        // Edge case: qty is 0
        arg = Arrays.asList("0", "buyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);

        // Error scenario: fiefdom does not belong to the kingdom
        arg = Arrays.asList("10", "buyer", "nonexistentFiefdom").iterator();
        purchase.castlePersonal(arg);
        // Assert that "Fiefdom doesn't belong to the kingdom" message is printed

        // Error scenario: buyer is not king or fiefdom is not empty or buyer is count or duke
        arg = Arrays.asList("10", "nonexistentBuyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);
        // Assert that proper Exception is thrown and caught
    }

    @Test
    public void testMercenary() {
        // Typical use case
        Iterator<String> arg = Arrays.asList("10", "buyer", "knight").iterator();
        purchase.mercenary(arg);

        // Edge case: qty is 0
        arg = Arrays.asList("0", "buyer", "knight").iterator();
        purchase.mercenary(arg);

        // Error scenario: buyer is not king or buyer and knight are different users
        arg = Arrays.asList("10", "nonexistentBuyer", "knight").iterator();
        purchase.mercenary(arg);
        // Assert that "Inexistent Knight" message is printed
    }
}