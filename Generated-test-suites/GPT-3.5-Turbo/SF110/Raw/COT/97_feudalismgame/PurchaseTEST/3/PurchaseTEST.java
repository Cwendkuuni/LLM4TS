package src;

import java.util.*;
import java.lang.reflect.*;
import org.junit.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // TODO: Test perform() functionality
    }

    @Test
    public void testVassal() {
        // TODO: Test vassal() functionality
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassal_NegativeQty() {
        // TODO: Test vassal() with negative quantity of vassals
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassal_NonExistentBuyerName() {
        // TODO: Test vassal() with non-existent buyer name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassal_NonExistentFiefdomName() {
        // TODO: Test vassal() with non-existent fiefdom name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassal_NonExistentFiefdomOwner() {
        // TODO: Test vassal() with non-existent fiefdom owner
    }

    @Test
    public void testCastleFiefdom() {
        // TODO: Test castleFiefdom() functionality
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdom_NegativeQty() {
        // TODO: Test castleFiefdom() with negative quantity of castle fiefdoms
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdom_NonExistentBuyerName() {
        // TODO: Test castleFiefdom() with non-existent buyer name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdom_NonExistentFiefdomName() {
        // TODO: Test castleFiefdom() with non-existent fiefdom name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdom_NonExistentFiefdomOwner() {
        // TODO: Test castleFiefdom() with non-existent fiefdom owner
    }

    @Test
    public void testCastlePersonal() {
        // TODO: Test castlePersonal() functionality
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastlePersonal_NegativeQty() {
        // TODO: Test castlePersonal() with negative quantity of personal castles
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastlePersonal_NonExistentBuyerName() {
        // TODO: Test castlePersonal() with non-existent buyer name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastlePersonal_NonExistentFiefdomName() {
        // TODO: Test castlePersonal() with non-existent fiefdom name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastlePersonal_NonExistentFiefdomOwner() {
        // TODO: Test castlePersonal() with non-existent fiefdom owner
    }

    @Test
    public void testMercenary() {
        // TODO: Test mercenary() functionality
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMercenary_NegativeQty() {
        // TODO: Test mercenary() with negative quantity of mercenaries
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMercenary_NonExistentBuyerName() {
        // TODO: Test mercenary() with non-existent buyer name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMercenary_NonExistentKnightName() {
        // TODO: Test mercenary() with non-existent knight name
    }

    

    // TODO: Add individual test methods here
    
    
}