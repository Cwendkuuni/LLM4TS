import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class PurchaseTest {

    private Purchase purchase;
    private Map<String, Fiefdoms> map;
    private Map<String, Knight> currentPlayers;

    @Before
    public void setUp() {
        purchase = new Purchase();
        map = new HashMap<>();
        currentPlayers = new HashMap<>();
    }

    @Test
    public void testVassal() {
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Fiefdoms fiefdom = new Fiefdoms();
        map.put("fiefdomName", fiefdom);
        Knight buyerObj = new Knight();
        currentPlayers.put("buyerName", buyerObj);
        
        purchase.vassal(arg);

        // Assert statements
    }

    @Test
    public void testCastleFiefdom() {
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Fiefdoms fiefdom = new Fiefdoms();
        map.put("fiefdomName", fiefdom);
        Knight buyerObj = new Knight();
        currentPlayers.put("buyerName", buyerObj);
        
        purchase.castleFiefdom(arg);

        // Assert statements
    }

    @Test
    public void testCastlePersonal() {
        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Fiefdoms fiefdom = new Fiefdoms();
        map.put("fiefdomName", fiefdom);
        Knight buyerObj = new Knight();
        currentPlayers.put("buyerName", buyerObj);
        
        purchase.castlePersonal(arg);

        // Assert statements
    }

    @Test
    public void testMercenary() {
        Iterator<String> arg = Arrays.asList("1", "buyerName", "knightName").iterator();
        Knight knight = new Knight();
        Knight buyerObj = new Knight();
        currentPlayers.put("buyerName", buyerObj);
        currentPlayers.put("knightName", knight);
        
        purchase.mercenary(arg);

        // Assert statements
    }
}