package src;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection<String> args = new ArrayList<>();
        args.add("methodName");
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("fiefdomName");
        
        purchase.vassal(arg);
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("fiefdomName");
        
        purchase.castleFiefdom(arg);
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("fiefdomName");
        
        purchase.castlePersonal(arg);
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("knightName");
        
        purchase.mercenary(arg);
    }
}