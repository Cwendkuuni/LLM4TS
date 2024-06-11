import org.junit.*;
import static org.junit.Assert.*;

public class PurchaseTest {

   @Test
   public void testPerform() {
      // typical use case
      Purchase purchase = new Purchase();
      Collection args = new ArrayList();
      args.add("vassal");
      args.add("10");
      args.add("buyer1");
      args.add("fiefdom1");
      purchase.perform(args);
      // edge case
      args = new ArrayList();
      args.add("castleFiefdom");
      args.add("5");
      args.add("buyer2");
      args.add("fiefdom2");
      purchase.perform(args);
      // error scenario
      args = new ArrayList();
      args.add("castlePersonal");
      args.add("2");
      args.add("buyer3");
      args.add("fiefdom3");
      purchase.perform(args);
      // error scenario
      args = new ArrayList();
      args.add("mercenary");
      args.add("3");
      args.add("buyer4");
      args.add("knight1");
      purchase.perform(args);
   }
   
   @Test
   public void testVassal() {
      // typical use case
      Purchase purchase = new Purchase();
      Iterator arg = new ArrayList().iterator();
      arg.add("10");
      arg.add("buyer1");
      arg.add("fiefdom1");
      purchase.vassal(arg);
      // edge case
      arg = new ArrayList().iterator();
      arg.add("2");
      arg.add("buyer2");
      arg.add("fiefdom2");
      purchase.vassal(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("5");
      arg.add("buyer3");
      arg.add("fiefdom3");
      purchase.vassal(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("3");
      arg.add("buyer4");
      arg.add("fiefdom4");
      purchase.vassal(arg);
   }
   
   @Test
   public void testCastleFiefdom() {
      // typical use case
      Purchase purchase = new Purchase();
      Iterator arg = new ArrayList().iterator();
      arg.add("5");
      arg.add("buyer1");
      arg.add("fiefdom1");
      purchase.castleFiefdom(arg);
      // edge case
      arg = new ArrayList().iterator();
      arg.add("3");
      arg.add("buyer2");
      arg.add("fiefdom2");
      purchase.castleFiefdom(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("7");
      arg.add("buyer3");
      arg.add("fiefdom3");
      purchase.castleFiefdom(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("4");
      arg.add("buyer4");
      arg.add("fiefdom4");
      purchase.castleFiefdom(arg);
   }
   
   @Test
   public void testCastlePersonal() {
      // typical use case
      Purchase purchase = new Purchase();
      Iterator arg = new ArrayList().iterator();
      arg.add("2");
      arg.add("buyer1");
      arg.add("fiefdom1");
      purchase.castlePersonal(arg);
      // edge case
      arg = new ArrayList().iterator();
      arg.add("4");
      arg.add("buyer2");
      arg.add("fiefdom2");
      purchase.castlePersonal(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("6");
      arg.add("buyer3");
      arg.add("fiefdom3");
      purchase.castlePersonal(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("3");
      arg.add("buyer4");
      arg.add("fiefdom4");
      purchase.castlePersonal(arg);
   }
   
   @Test
   public void testMercenary() {
      // typical use case
      Purchase purchase = new Purchase();
      Iterator arg = new ArrayList().iterator();
      arg.add("3");
      arg.add("buyer1");
      arg.add("knight1");
      purchase.mercenary(arg);
      // edge case
      arg = new ArrayList().iterator();
      arg.add("5");
      arg.add("buyer2");
      arg.add("knight2");
      purchase.mercenary(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("2");
      arg.add("buyer3");
      arg.add("knight3");
      purchase.mercenary(arg);
      // error scenario
      arg = new ArrayList().iterator();
      arg.add("4");
      arg.add("buyer4");
      arg.add("knight4");
      purchase.mercenary(arg);
   }
}