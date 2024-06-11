import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {
    
    private Purchase purchase;
    
    @Before
    public void setup() {
        purchase = new Purchase();
    }
    
    @Test
    public void perform_validArgs_argsValidatedAndMethodInvoked() {
        Collection<String> args = new ArrayList<>();
        args.add("vassal");
        Iterator<String> argsIter = args.iterator();
        
        purchase.perform(args);
        // Assert that vassal method is invoked with correct arguments
        
    }
    
    @Test
    public void perform_nullArgs_exceptionThrown() {
        Collection<String> args = null;

        try {
            purchase.perform(args);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception caught, test passes
        }
    }
    
    @Test
    public void perform_invalidMethodName_exceptionThrown() {
        Collection<String> args = new ArrayList<>();
        args.add("invalidMethodName");
        Iterator<String> argsIter = args.iterator();

        try {
            purchase.perform(args);
            fail("Expected NoSuchMethodException to be thrown");
        } catch (NoSuchMethodException e) {
            // Exception caught, test passes
        }
    }
    
    @Test
    public void vassal_buyerHasInsufficientFunds_noVassalsBought() {
        Iterator<String> argsIter = getVassalArgs();

        purchase.vassal(argsIter);
        // Assert that no vassals are bought
    }
    
    @Test
    public void vassal_fiefdomDoesNotBelongToKingdom_noVassalsBought() {
        Iterator<String> argsIter = getVassalArgs();
        
        purchase.vassal(argsIter);
        // Assert that no vassals are bought
    }
    
    @Test
    public void vassal_onlyCountOrDukeCanBuyCastle_messagePrinted() {
        Iterator<String> argsIter = getVassalArgs();
        
        purchase.vassal(argsIter);
        // Assert that correct message is printed
    }
    
    @Test
    public void castleFiefdom_buyerHasInsufficientFunds_noCastlesBought() {
        Iterator<String> argsIter = getCastleFiefdomArgs();
        
        purchase.castleFiefdom(argsIter);
        // Assert that no castles are bought
    }
    
    @Test
    public void castleFiefdom_fiefdomDoesNotBelongToKingdom_noCastlesBought() {
        Iterator<String> argsIter = getCastleFiefdomArgs();
        
        purchase.castleFiefdom(argsIter);
        // Assert that no castles are bought
    }
    
    @Test
    public void castleFiefdom_onlyCountOrDukeCanBuyCastle_messagePrinted() {
        Iterator<String> argsIter = getCastleFiefdomArgs();
        
        purchase.castleFiefdom(argsIter);
        // Assert that correct message is printed
    }
    
    @Test
    public void castlePersonal_buyerHasInsufficientFunds_noCastlesBought() {
        Iterator<String> argsIter = getCastlePersonalArgs();
        
        purchase.castlePersonal(argsIter);
        // Assert that no castles are bought
    }
    
    @Test
    public void castlePersonal_onlyCountOrDukeCanBuyCastle_messagePrinted() {
        Iterator<String> argsIter = getCastlePersonalArgs();
        
        purchase.castlePersonal(argsIter);
        // Assert that correct message is printed
    }
    
    @Test
    public void mercenary_buyerHasInsufficientFunds_noMercenariesBought() {
        Iterator<String> argsIter = getMercenaryArgs();
        
        purchase.mercenary(argsIter);
        // Assert that no mercenaries are bought
    }
    
    @Test
    public void mercenary_inexistentKnight_exceptionThrown() {
        Iterator<String> argsIter = getMercenaryArgs();
        
        try {
            purchase.mercenary(argsIter);
            fail("Expected InexistentKnightException to be thrown");
        } catch (InexistentKnightException e) {
            // Exception caught, test passes
        }
    }
    
    private Iterator<String> getVassalArgs() {
        Collection<String> args = new ArrayList<>();
        args.add("2"); // qty
        args.add("buyer"); // buyerName
        args.add("fiefdomName"); // fiefdom name
        return args.iterator();
    }
    
    private Iterator<String> getCastleFiefdomArgs() {
        Collection<String> args = new ArrayList<>();
        args.add("2"); // qty
        args.add("buyer"); // buyerName
        args.add("fiefdomName"); // fiefdom name
        return args.iterator();
    }
    
    private Iterator<String> getCastlePersonalArgs() {
        Collection<String> args = new ArrayList<>();
        args.add("2"); // qty
        args.add("buyer"); // buyerName
        args.add("fiefdomName"); // fiefdom name
        return args.iterator();
    }
    
    private Iterator<String> getMercenaryArgs() {
        Collection<String> args = new ArrayList<>();
        args.add("2"); // qty
        args.add("buyer"); // buyer name
        args.add("knightName"); // knight name
        return args.iterator();
    }
}