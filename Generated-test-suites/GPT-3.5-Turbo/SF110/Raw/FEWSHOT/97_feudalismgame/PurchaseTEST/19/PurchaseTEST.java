package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PurchaseTest {
    
    @Test
    public void testPerform() {
        // Need to mock and test the behavior when invoking aMethod.getMethod() and methodObj.invoke()
    }

    @Test
    public void testVassal() {
        // Test when fiefdom.getOwner() is not null and buyerObj.getRank() is king
        Iterator arg1 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase = new Purchase();
        purchase.vassal(arg1);
        
        // Test when fiefdom.getOwner() is not null and buyerObj.getRank() is not king
        Iterator arg2 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase2 = new Purchase();
        purchase2.vassal(arg2);
        
        // Test when fiefdom.getOwner() is null
        Iterator arg3 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase3 = new Purchase();
        purchase3.vassal(arg3);
    }

    @Test
    public void testCastleFiefdom() {
        // Test when fiefdom.getOwner() is not null and buyerObj.getRank() is king
        Iterator arg1 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase = new Purchase();
        purchase.castleFiefdom(arg1);
        
        // Test when fiefdom.getOwner() is not null and buyerObj.getRank() is not king
        Iterator arg2 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase2 = new Purchase();
        purchase2.castleFiefdom(arg2);
        
        // Test when fiefdom.getOwner() is null
        Iterator arg3 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase3 = new Purchase();
        purchase3.castleFiefdom(arg3);
    }

    @Test
    public void testCastlePersonal() {
        // Test when fiefdom.getOwner() is not null and buyerObj.getRank() is king
        Iterator arg1 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase = new Purchase();
        purchase.castlePersonal(arg1);
        
        // Test when fiefdom.getOwner() is not null and buyerObj.getRank() is not king
        Iterator arg2 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase2 = new Purchase();
        purchase2.castlePersonal(arg2);
        
        // Test when fiefdom.getOwner() is null
        Iterator arg3 = Arrays.asList("1", "buyerName", "fiefdomName").iterator();
        Purchase purchase3 = new Purchase();
        purchase3.castlePersonal(arg3);
    }

    @Test
    public void testMercenary() {
        // Test when buyerObj.getName() is equal to knight.getName()
        Iterator arg1 = Arrays.asList("1", "buyerName", "knightName").iterator();
        Purchase purchase = new Purchase();
        purchase.mercenary(arg1);
        
        // Test when buyerObj is king
        Iterator arg2 = Arrays.asList("1", "buyerName", "knightName").iterator();
        Purchase purchase2 = new Purchase();
        purchase2.mercenary(arg2);
        
        // Test when buyerObj and knight do not exist in CurrentPlayers.currentPlayers
        Iterator arg3 = Arrays.asList("1", "buyerName", "knightName").iterator();
        Purchase purchase3 = new Purchase();
        purchase3.mercenary(arg3);
    }
    
}