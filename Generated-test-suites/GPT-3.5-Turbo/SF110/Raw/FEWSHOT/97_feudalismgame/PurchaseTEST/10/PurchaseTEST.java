
package src;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Test case 1
        //TODO: Add test inputs
        Purchase purchase1 = new Purchase();
        List<String> args1 = new ArrayList<>();
        //TODO: Add arguments to args1
        purchase1.perform(args1);
        //TODO: Add assertions

        // Test case 2
        //TODO: Add test inputs
        Purchase purchase2 = new Purchase();
        List<String> args2 = new ArrayList<>();
        //TODO: Add arguments to args2
        purchase2.perform(args2);
        //TODO: Add assertions
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();

        // Test case 1
        //TODO: Add test inputs
        Iterator arg1 = null;
        purchase.vassal(arg1);
        //TODO: Add assertions

        // Test case 2
        //TODO: Add test inputs
        Iterator arg2 = null;
        purchase.vassal(arg2);
        //TODO: Add assertions
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();

        // Test case 1
        //TODO: Add test inputs
        Iterator arg1 = null;
        purchase.castleFiefdom(arg1);
        //TODO: Add assertions

        // Test case 2
        //TODO: Add test inputs
        Iterator arg2 = null;
        purchase.castleFiefdom(arg2);
        //TODO: Add assertions
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();

        // Test case 1
        //TODO: Add test inputs
        Iterator arg1 = null;
        purchase.castlePersonal(arg1);
        //TODO: Add assertions

        // Test case 2
        //TODO: Add test inputs
        Iterator arg2 = null;
        purchase.castlePersonal(arg2);
        //TODO: Add assertions
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();

        // Test case 1
        //TODO: Add test inputs
        Iterator arg1 = null;
        purchase.mercenary(arg1);
        //TODO: Add assertions

        // Test case 2
        //TODO: Add test inputs
        Iterator arg2 = null;
        purchase.mercenary(arg2);
        //TODO: Add assertions
    }
}
