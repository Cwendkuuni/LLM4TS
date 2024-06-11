import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection args = new ArrayList();
        args.add("vassal");
        args.add(Arrays.asList("1", "King", "exampleFiefdom"));
      
        purchase.perform(args);
        // Add assertions
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList("1", "King", "exampleFiefdom").iterator();
      
        purchase.vassal(arg);
        // Add assertions
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList("1", "King", "exampleFiefdom").iterator();
      
        purchase.castleFiefdom(arg);
        // Add assertions
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList("1", "King", "exampleFiefdom").iterator();
      
        purchase.castlePersonal(arg);
        // Add assertions
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator arg = Arrays.asList("1", "buyerName", "knightName").iterator();
      
        purchase.mercenary(arg);
        // Add assertions
    }
}