package src;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator iterator = Arrays.asList("2", "buyerName", "fiefdom").iterator();
        purchase.vassal(iterator);
    }
    
    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator iterator = Arrays.asList("3", "buyerName", "fiefdom").iterator();
        purchase.castleFiefdom(iterator);
    }
    
    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator iterator = Arrays.asList("1", "buyerName", "fiefdom").iterator();
        purchase.castlePersonal(iterator);
    }
    
    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator iterator = Arrays.asList("5", "buyerName", "knight").iterator();
        purchase.mercenary(iterator);
    }
}