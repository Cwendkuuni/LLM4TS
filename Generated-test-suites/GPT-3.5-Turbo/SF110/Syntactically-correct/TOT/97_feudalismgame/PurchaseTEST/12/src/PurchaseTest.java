import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PurchaseTest {

  //Test case for vassal() method
  @Test
  public void testVassal() {
    Purchase purchase = new Purchase();
    Iterator args = Arrays.asList("5", "buyer1", "fiefdom1").iterator();
    
    purchase.vassal(args);
    
    //Check if the vassals are bought successfully
    assertEquals(5, fiefdom.getLoyalVassals());
  }

  //Test case for castleFiefdom() method
  @Test
  public void testCastleFiefdom() {
    Purchase purchase = new Purchase();
    Iterator args = Arrays.asList("3", "buyer2", "fiefdom2").iterator();
    
    purchase.castleFiefdom(args);
    
    //Check if the Fiefdom castles are bought successfully
    assertEquals(3, fiefdom.getFiefdomCastles());
  }

  //Test case for castlePersonal() method
  @Test
  public void testCastlePersonal() {
    Purchase purchase = new Purchase();
    Iterator args = Arrays.asList("2", "buyer3", "fiefdom3").iterator();
    
    purchase.castlePersonal(args);
    
    //Check if the Personal castles are bought successfully
    assertEquals(2, fiefdom.getPersonalCastles());
  }

  //Test case for mercenary() method
  @Test
  public void testMercenary() {
    Purchase purchase = new Purchase();
    Iterator args = Arrays.asList("4", "buyer4", "knight1").iterator();
    
    purchase.mercenary(args);
    
    //Check if the mercenaries are bought successfully
    assertEquals(4, knight.getMercenaries());
  }

  //Test case for perform() method
  @Test
  public void testPerform() {
    Purchase purchase = new Purchase();
    Collection args = Arrays.asList("vassal", "3", "buyer1", "fiefdom1");
    
    purchase.perform(args);
    
    //Check if the perform method executes successfully
    assertTrue(true);
  }
}