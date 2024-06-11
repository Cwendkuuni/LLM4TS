import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.lang.reflect.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        Iterator arg = Arrays.asList("2", "buyer", "fiefdom").iterator();
        Map.map.put("fiefdom", new Fiefdoms());
        CurrentPlayers.currentPlayers.put("buyer", new Knight("buyer"));
        Purchase purchase = new Purchase();
        
        // Typical use case
        purchase.vassal(arg);
        assertEquals(2, fiefdom.getLoyalVassals());
        assertEquals(false, fiefdom.isFiefdomMine());
        
        // Edge case: zero quantity
        arg = Arrays.asList("0", "buyer", "fiefdom").iterator();
        purchase.vassal(arg);
        assertEquals(2, fiefdom.getLoyalVassals());
        assertEquals(false, fiefdom.isFiefdomMine());
        
        // Error scenario: insufficient funds
        arg = Arrays.asList("5", "buyer", "fiefdom").iterator();
        CurrentPlayers.currentPlayers.get("buyer").setTotalMoney(0, true);
        purchase.vassal(arg);
        assertEquals(2, fiefdom.getLoyalVassals());
        assertEquals(false, fiefdom.isFiefdomMine());
    }
    
    @Test
    public void testCastleFiefdom() {
        Iterator arg = Arrays.asList("3", "buyer", "fiefdom").iterator();
        Map.map.put("fiefdom", new Fiefdoms());
        CurrentPlayers.currentPlayers.put("buyer", new Knight("buyer"));
        Purchase purchase = new Purchase();
        
        // Typical use case
        purchase.castleFiefdom(arg);
        assertEquals(3, fiefdom.getFiefdomCastles());
        assertEquals(false, fiefdom.isFiefdomMine());
        
        // Edge case: zero quantity
        arg = Arrays.asList("0", "buyer", "fiefdom").iterator();
        purchase.castleFiefdom(arg);
        assertEquals(3, fiefdom.getFiefdomCastles());
        assertEquals(false, fiefdom.isFiefdomMine());
        
        // Error scenario: insufficient funds
        arg = Arrays.asList("4", "buyer", "fiefdom").iterator();
        CurrentPlayers.currentPlayers.get("buyer").setTotalMoney(0, true);
        purchase.castleFiefdom(arg);
        assertEquals(3, fiefdom.getFiefdomCastles());
        assertEquals(false, fiefdom.isFiefdomMine());
    }
    
    @Test
    public void testCastlePersonal() {
        Iterator arg = Arrays.asList("1", "buyer", "fiefdom").iterator();
        Map.map.put("fiefdom", new Fiefdoms());
        CurrentPlayers.currentPlayers.put("buyer", new Knight("buyer"));
        Purchase purchase = new Purchase();
        
        // Typical use case
        purchase.castlePersonal(arg);
        assertEquals(1, fiefdom.getPersonalCastles());
        assertEquals(false, fiefdom.isFiefdomMine());
        
        // Edge case: zero quantity
        arg = Arrays.asList("0", "buyer", "fiefdom").iterator();
        purchase.castlePersonal(arg);
        assertEquals(1, fiefdom.getPersonalCastles());
        assertEquals(false, fiefdom.isFiefdomMine());
        
        // Error scenario: insufficient funds
        arg = Arrays.asList("2", "buyer", "fiefdom").iterator();
        CurrentPlayers.currentPlayers.get("buyer").setTotalMoney(0, true);
        purchase.castlePersonal(arg);
        assertEquals(1, fiefdom.getPersonalCastles());
        assertEquals(false, fiefdom.isFiefdomMine());
    }
    
    @Test
    public void testMercenary() {
        CurrentPlayers.currentPlayers.put("buyer", new Knight("buyer"));
        CurrentPlayers.currentPlayers.put("knight", new Knight("knight"));
        Iterator arg = Arrays.asList("4", "buyer", "knight").iterator();
        Purchase purchase = new Purchase();
        
        // Typical use case
        purchase.mercenary(arg);
        assertEquals(4, knight.getMercenaries());
        assertEquals(true, knight.isKnightMine());
        
        // Error scenario: insufficient funds
        arg = Arrays.asList("5", "buyer", "knight").iterator();
        CurrentPlayers.currentPlayers.get("buyer").setTotalMoney(0, true);
        purchase.mercenary(arg);
        assertEquals(4, knight.getMercenaries());
        assertEquals(true, knight.isKnightMine());
        
        // Error scenario: Inexistent Knight
        arg = Arrays.asList("3", "buyer", "fakeKnight").iterator();
        purchase.mercenary(arg);
        assertEquals(4, knight.getMercenaries());
        assertEquals(true, knight.isKnightMine());
    }
}