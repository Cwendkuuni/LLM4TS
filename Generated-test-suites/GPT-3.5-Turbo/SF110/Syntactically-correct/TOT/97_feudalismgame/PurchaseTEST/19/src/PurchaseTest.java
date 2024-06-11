
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

  @Test // Test Purchase.perform() with typical use case
  public void testPerformTypical() {
    Purchase purchase = new Purchase();
    List<String> args = new ArrayList<>();
    args.add("vassal");
    args.add("5");
    args.add("buyerName");
    args.add("fiefdomName");
    purchase.perform(args);
    // assert the expected results
  }

  @Test // Test Purchase.perform() with edge case - empty args
  public void testPerformEmptyArgs() {
    Purchase purchase = new Purchase();
    List<String> args = new ArrayList<>();
    purchase.perform(args);
    // assert the expected results
  }

  @Test // Test Purchase.perform() with error scenario - ClassNotFoundException
  public void testPerformClassNotFoundException() {
    Purchase purchase = new Purchase();
    List<String> args = new ArrayList<>();
    args.add("invalidMethodName");
    purchase.perform(args);
    // assert the expected results
  }

  @Test // Test Purchase.vassal() with typical use case
  public void testVassalTypical() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    purchase.vassal(arg);
    // assert the expected results
  }

  @Test // Test Purchase.vassal() with edge case - buyerObj.getRank() = "king"
  public void testVassalEdgeCase() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    // set buyerObj.getRank() as "king"
    purchase.vassal(arg);
    // assert the expected results
  }

  @Test // Test Purchase.vassal() with error scenario - buyerObj.getRank() != "king" or fiefdom.getOwner() == null
  public void testVassalErrorScenario() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    // set buyerObj.getRank() as "count"
    purchase.vassal(arg);
    // assert the expected results
  }
  
  @Test // Test Purchase.castleFiefdom() with typical use case
  public void testCastleFiefdomTypical() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    purchase.castleFiefdom(arg);
    // assert the expected results
  }

  @Test // Test Purchase.castleFiefdom() with edge case - buyerObj.getRank() = "king" and fiefdom.getViking().size() == 0 and fiefdom.getMagyar().size() == 0 and fiefdom.getMuslim().size() == 0
  public void testCastleFiefdomEdgeCase() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    // set buyerObj.getRank() as "king" and fiefdom.getViking().size() == 0 and fiefdom.getMagyar().size() == 0 and fiefdom.getMuslim().size() == 0
    purchase.castleFiefdom(arg);
    // assert the expected results
  }

  @Test // Test Purchase.castleFiefdom() with error scenario - buyerObj.getRank() != "king" or (buyerObj.isFiefdomMine(fiefdom.getName()) and fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size() != 0)
  public void testCastleFiefdomErrorScenario() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    // set buyerObj.getRank() as "duke" or (buyerObj.isFiefdomMine(fiefdom.getName()) and fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size() != 0)
    purchase.castleFiefdom(arg);
    // assert the expected results
  }

  @Test // Test Purchase.castlePersonal() with typical use case
  public void testCastlePersonalTypical() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    purchase.castlePersonal(arg);
    // assert the expected results
  }

  @Test // Test Purchase.castlePersonal() with edge case - buyerObj.getRank() = "king" and CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank() = "knight"
  public void testCastlePersonalEdgeCase() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    // set buyerObj.getRank() as "king" and CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank() as "knight"
    purchase.castlePersonal(arg);
    // assert the expected results
  }

  @Test // Test Purchase.castlePersonal() with error scenario - buyerObj.getRank() != "king" or (buyerObj.isFiefdomMine(fiefdom.getName()) and fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size() != 0) or buyerObj.getRank() = "king" 
  public void testCastlePersonalErrorScenario() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
    // set buyerObj.getRank() as "count" or (buyerObj.isFiefdomMine(fiefdom.getName()) and fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size() != 0) or set buyerObj.getRank() as "king" 
    purchase.castlePersonal(arg);
    // assert the expected results
  }

  @Test // Test Purchase.mercenary() with typical use case
  public void testMercenaryTypical() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "buyerName", "knightName").iterator();
    purchase.mercenary(arg);
    // assert the expected results
  }

  @Test // Test Purchase.mercenary() with error scenario - buyerObj or knight not found in CurrentPlayers.currentPlayers map
  public void testMercenaryErrorScenario() {
    Purchase purchase = new Purchase();
    Iterator<String> arg = Arrays.asList("5", "invalidBuyerName", "knightName").iterator();
    purchase.mercenary(arg);
    // assert the expected results
  }
}

