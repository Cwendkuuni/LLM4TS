import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {
    private Purchase purchase;
    private Iterator argIterator;

    @Before
    public void setup() {
        purchase = new Purchase();
        argIterator = Collections.emptyIterator();
    }

    @Test
    public void testVassal() {
        // Test case 1
        argIterator = Arrays.asList("10", "buyerName", "fiefdomName").iterator();
        purchase.vassal(argIterator);
        // Write assertions here
        // ...

        // Test case 2
        argIterator = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.vassal(argIterator);
        // Write assertions here
        // ...
    }

    @Test
    public void testCastleFiefdom() {
        // Test case 1
        argIterator = Arrays.asList("10", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(argIterator);
        // Write assertions here
        // ...

        // Test case 2
        argIterator = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.castleFiefdom(argIterator);
        // Write assertions here
        // ...
    }

    @Test
    public void testCastlePersonal() {
        // Test case 1
        argIterator = Arrays.asList("10", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(argIterator);
        // Write assertions here
        // ...

        // Test case 2
        argIterator = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        purchase.castlePersonal(argIterator);
        // Write assertions here
        // ...
    }

    @Test
    public void testMercenary() {
        // Test case 1
        argIterator = Arrays.asList("10", "buyerName", "knightName").iterator();
        purchase.mercenary(argIterator);
        // Write assertions here
        // ...

        // Test case 2
        argIterator = Arrays.asList("5", "buyerName", "knightName").iterator();
        purchase.mercenary(argIterator);
        // Write assertions here
        // ...
    }
}