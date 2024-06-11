import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseTest {

  Purchase purchase = new Purchase();
    
  // Test case for perform() method
  @Test
  public void testPerform() {
    // typical use case
    // create a collection with arguments
    Collection<String> args = new ArrayList<>();
    args.add("vassal");
    args.add("10");
    args.add("buyerName");
    args.add("fiefdomName");
    purchase.perform(args);

    // edge case
    // create a collection with only one argument
    Collection<String> args2 = new ArrayList<>();
    args2.add("vassal");
    purchase.perform(args2);

    // error scenario
    // create a collection with the wrong arguments
    Collection<Integer> args3 = new ArrayList<>();
    args3.add(10);
    args3.add(20);
    args3.add(30);
    args3.add(40);
    purchase.perform(args3);
  }
  
  // Test case for vassal() method
  @Test
  public void testVassal() {
    Purchase purchase = new Purchase();
    // typical use case
    Iterator<String> arg = new ArrayList<>().iterator();
    arg.add("10");
    arg.add("buyerName");
    arg.add("fiefdomName");
    purchase.vassal(arg);

    // edge case
    Iterator<String> arg2 = new ArrayList<>().iterator();
    arg2.add("1");
    arg2.add("buyerName");
    arg2.add("fiefdomName");
    purchase.vassal(arg2);

    // error scenario
    Iterator<Integer> arg3 = new ArrayList<>().iterator();
    arg3.add(10);
    arg3.add(20);
    arg3.add(30);
    purchase.vassal(arg3);
  }

  // Test case for castleFiefdom() method
  @Test
  public void testCastleFiefdom() {
    // typical use case
    Iterator<String> arg = new ArrayList<>().iterator();
    arg.add("10");
    arg.add("buyerName");
    arg.add("fiefdomName");
    purchase.castleFiefdom(arg);

    // edge case
    Iterator<String> arg2 = new ArrayList<>().iterator();
    arg2.add("1");
    arg2.add("buyerName");
    arg2.add("fiefdomName");
    purchase.castleFiefdom(arg2);

    // error scenario
    Iterator<Integer> arg3 = new ArrayList<>().iterator();
    arg3.add(10);
    arg3.add(20);
    arg3.add(30);
    purchase.castleFiefdom(arg3);
  }
  
  // Test case for castlePersonal() method
  @Test
  public void testCastlePersonal() {
    // typical use case
    Iterator<String> arg = new ArrayList<>().iterator();
    arg.add("10");
    arg.add("buyerName");
    arg.add("fiefdomName");
    purchase.castlePersonal(arg);

    // edge case
    Iterator<String> arg2 = new ArrayList<>().iterator();
    arg2.add("1");
    arg2.add("buyerName");
    arg2.add("fiefdomName");
    purchase.castlePersonal(arg2);

    // error scenario
    Iterator<Integer> arg3 = new ArrayList<>().iterator();
    arg3.add(10);
    arg3.add(20);
    arg3.add(30);
    purchase.castlePersonal(arg3);
  }

  // Test case for mercenary() method
  @Test
  public void testMercenary() {
    // typical use case
    Iterator<String> arg = new ArrayList<>().iterator();
    arg.add("10");
    arg.add("buyerName");
    arg.add("knightName");
    purchase.mercenary(arg);

    // edge case
    Iterator<String> arg2 = new ArrayList<>().iterator();
    arg2.add("1");
    arg2.add("buyerName");
    arg2.add("knightName");
    purchase.mercenary(arg2);

    // error scenario
    Iterator<Integer> arg3 = new ArrayList<>().iterator();
    arg3.add(10);
    arg3.add(20);
    arg3.add(30);
    purchase.mercenary(arg3);
  }
}