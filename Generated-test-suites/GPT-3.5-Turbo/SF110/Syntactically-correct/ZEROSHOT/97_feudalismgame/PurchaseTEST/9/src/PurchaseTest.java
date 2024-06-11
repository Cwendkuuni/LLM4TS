import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // TODO: Implement test for perform method
        fail("Not implemented yet");
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyer", "fiefdom").iterator();

        // Test case 1
        purchase.vassal(arg);
        // TODO: Add assertions

        // Test case 2
        // TODO: Create new test case

        // ...
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyer", "fiefdom").iterator();

        // Test case 1
        purchase.castleFiefdom(arg);
        // TODO: Add assertions

        // Test case 2
        // TODO: Create new test case

        // ...
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyer", "fiefdom").iterator();

        // Test case 1
        purchase.castlePersonal(arg);
        // TODO: Add assertions

        // Test case 2
        // TODO: Create new test case

        // ...
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = Arrays.asList("5", "buyer", "knight").iterator();

        // Test case 1
        purchase.mercenary(arg);
        // TODO: Add assertions

        // Test case 2
        // TODO: Create new test case

        // ...
    }

}