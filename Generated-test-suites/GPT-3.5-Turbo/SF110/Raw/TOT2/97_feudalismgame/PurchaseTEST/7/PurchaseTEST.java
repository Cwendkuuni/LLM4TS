import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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
        Collection<String> args = new ArrayList<>();
        args.add("vassal");
        args.add("1");
        args.add("buyerName");
        args.add("fiefdomName");
        purchase.perform(args);
        
        // Edge case
        args.clear();
        args.add("castlePersonal");
        args.add("2");
        args.add("buyerName");
        args.add("fiefdomName");
        purchase.perform(args);

        // Error scenario
        args.clear();
        args.add("invalidMethod");
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        // Typical use case
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("fiefdomName");
        purchase.vassal(arg);

        // Edge case
        arg = new ArrayList<String>().iterator();
        arg.add("0");
        arg.add("buyerName");
        arg.add("fiefdomName");
        purchase.vassal(arg);

        // Error scenario
        arg = new ArrayList<String>().iterator();
        arg.add("notAnInt");
        purchase.vassal(arg);
    }

    @Test
    public void testCastleFiefdom() {
        // Typical use case
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("2");
        arg.add("buyerName");
        arg.add("fiefdomName");
        purchase.castleFiefdom(arg);

        // Edge case
        arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("fiefdomName");
        purchase.castleFiefdom(arg);

        // Error scenario
        arg = new ArrayList<String>().iterator();
        arg.add("notAnInt");
        purchase.castleFiefdom(arg);
    }

    @Test
    public void testCastlePersonal() {
        // Typical use case
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("3");
        arg.add("buyerName");
        arg.add("fiefdomName");
        purchase.castlePersonal(arg);

        // Edge case
        arg = new ArrayList<String>().iterator();
        arg.add("1");
        arg.add("buyerName");
        arg.add("fiefdomName");
        purchase.castlePersonal(arg);

        // Error scenario
        arg = new ArrayList<String>().iterator();
        arg.add("notAnInt");
        purchase.castlePersonal(arg);
    }

    @Test
    public void testMercenary() {
        // Typical use case
        Iterator<String> arg = new ArrayList<String>().iterator();
        arg.add("4");
        arg.add("buyerName");
        arg.add("knightName");
        purchase.mercenary(arg);

        // Edge case
        arg = new ArrayList<String>().iterator();
        arg.add("0");
        arg.add("buyerName");
        arg.add("knightName");
        purchase.mercenary(arg);

        // Error scenario
        arg = new ArrayList<String>().iterator();
        arg.add("invalidArg");
        purchase.mercenary(arg);
    }
}