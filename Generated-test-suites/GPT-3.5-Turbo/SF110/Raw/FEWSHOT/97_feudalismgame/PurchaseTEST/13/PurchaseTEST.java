import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Iterator;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Setup
        Purchase purchase = new Purchase();

        // Test case 1
        Collection<String> args1 = new ArrayList<>();
        args1.add("vassal");
        args1.add("3");
        args1.add("buyerName");
        args1.add("fiefdom");
        purchase.perform(args1);
        // Assertions
        // assertEquals();

        // Test case 2
        Collection<String> args2 = new ArrayList<>();
        args2.add("castleFiefdom");
        args2.add("2");
        args2.add("buyerName");
        args2.add("fiefdom");
        purchase.perform(args2);
        // Assertions
        // assertEquals();

        // Test case 3
        Collection<String> args3 = new ArrayList<>();
        args3.add("castlePersonal");
        args3.add("4");
        args3.add("buyerName");
        args3.add("fiefdom");
        purchase.perform(args3);
        // Assertions
        // assertEquals();

        // Test case 4
        Collection<String> args4 = new ArrayList<>();
        args4.add("mercenary");
        args4.add("5");
        args4.add("buyerName");
        args4.add("knight");
        args4.add("buyerName");
        purchase.perform(args4);
        // Assertions
        // assertEquals();
    }

    @Test
    public void testVassal() {
        // Setup
        Purchase purchase = new Purchase();
        Iterator args = new Iterator();

        // Test case 1
        purchase.vassal(args);
        // Assertions
        // assertEquals();

        // Test case 2
        purchase.vassal(args);
        // Assertions
        // assertEquals();

        // Test case 3
        purchase.vassal(args);
        // Assertions
        // assertEquals();
    }

    @Test
    public void testCastleFiefdom() {
        // Setup
        Purchase purchase = new Purchase();
        Iterator args = new Iterator();

        // Test case 1
        purchase.castleFiefdom(args);
        // Assertions
        // assertEquals();

        // Test case 2
        purchase.castleFiefdom(args);
        // Assertions
        // assertEquals();

        // Test case 3
        purchase.castleFiefdom(args);
        // Assertions
        // assertEquals();
    }

    @Test
    public void testCastlePersonal() {
        // Setup
        Purchase purchase = new Purchase();
        Iterator args = new Iterator();

        // Test case 1
        purchase.castlePersonal(args);
        // Assertions
        // assertEquals();

        // Test case 2
        purchase.castlePersonal(args);
        // Assertions
        // assertEquals();

        // Test case 3
        purchase.castlePersonal(args);
        // Assertions
        // assertEquals();
    }

    @Test
    public void testMercenary() {
        // Setup
        Purchase purchase = new Purchase();
        Iterator args = new Iterator();

        // Test case 1
        purchase.mercenary(args);
        // Assertions
        // assertEquals();

        // Test case 2
        purchase.mercenary(args);
        // Assertions
        // assertEquals();

        // Test case 3
        purchase.mercenary(args);
        // Assertions
        // assertEquals();
    }
}