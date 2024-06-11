import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseTest {

    @Test
    public void testPerform_InvalidArgs_ThrowsException() {
        try {
            Purchase purchase = new Purchase();
            purchase.perform(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }

    @Test
    public void testVassal_BuyerHasInsufficientFunds_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        // Set buyer's funds to 0
        CurrentPlayers.currentPlayers.get("buyerName").setTotalMoney(0, false);
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.vassal(args);
        
        assertEquals("You cannot buy more vassals, you have insufficient funds\n", outContent.toString());
    }

    @Test
    public void testVassal_FiefdomDoesNotBelongToKingdom_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "nonExistentFiefdom").iterator();
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.vassal(args);
        
        assertEquals("Fiefdom doesn't belong to the kingdom\n", outContent.toString());
    }

    @Test
    public void testCastlePersonal_BuyerHasInsufficientFunds_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        // Set buyer's funds to 0
        CurrentPlayers.currentPlayers.get("buyerName").setTotalMoney(0, false);
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.castlePersonal(args);
        
        assertEquals("You cannot buy more Personal castle, you have insufficient funds\n", outContent.toString());
    }

    @Test
    public void testCastlePersonal_BuyerIsKingTriesToBuyForCountOrDuke_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.castlePersonal(args);
        
        assertEquals("You cannot buy a Personal Castle for Counts or Dukes\n", outContent.toString());
    }

    @Test
    public void testCastleFiefdom_BuyerHasInsufficientFunds_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        // Set buyer's funds to 0
        CurrentPlayers.currentPlayers.get("buyerName").setTotalMoney(0, false);
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.castleFiefdom(args);
        
        assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds\n", outContent.toString());
    }

    @Test
    public void testCastleFiefdom_BuyerIsKingTriesToBuyForCountOrDuke_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.castleFiefdom(args);
        
        assertEquals("You can only buy a castle for a fiefdom that belongs to you\n", outContent.toString());
    }

    @Test
    public void testMercenary_BuyerHasInsufficientFunds_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "knightName").iterator();
        
        // Set buyer's funds to 0
        CurrentPlayers.currentPlayers.get("buyerName").setTotalMoney(0, false);
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.mercenary(args);
        
        assertEquals("You cannot buy more mercenaries, you have insufficient funds\n", outContent.toString());
    }

    @Test
    public void testMercenary_KnightDoesNotExist_PrintsErrorMessage() {
        Purchase purchase = new Purchase();
        Iterator args = Arrays.asList("1", "buyerName", "nonExistentKnight").iterator();
        
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        purchase.mercenary(args);
        
        assertEquals("Inexistent Knight\n", outContent.toString());
    }
}