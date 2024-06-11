import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;

    @Before
    public void setUp() {
        purchase = Mockito.spy(new Purchase());
    }

    @Test
    public void testPerform() {
        Collection args = Mockito.mock(Collection.class);
        Iterator iterator = Mockito.mock(Iterator.class);
        Mockito.when(args.iterator()).thenReturn(iterator);

        try {
            Class aMethod = Class.forName("feudalism.Purchase");
            Method methodObj = aMethod.getMethod("", Iterator.class);
            purchase.perform(args);
            verify(purchase, times(1)).getClass();
            verify(aMethod, times(1)).getMethod("", Iterator.class);
            verify(methodObj, times(1)).invoke(purchase, iterator);
            verifyStatic(times(1));
            GameAutoActions.saveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVassal() {
        Iterator iterator = Mockito.mock(Iterator.class);
        Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        Knight buyerObj = Mockito.mock(Knight.class);
        Map.map.put("fiefdomName", fiefdom);
        CurrentPlayers.currentPlayers.put("buyerName", buyerObj);
        when(iterator.next()).thenReturn("10").thenReturn("buyerName").thenReturn("fiefdomName");
        when(fiefdom.getOwner()).thenReturn("ownerName");
        when(buyerObj.getRank()).thenReturn("king");
        purchase.vassal(iterator);
        verify(fiefdom, times(1)).getOwner();
        verify(buyerObj, times(1)).getRank();
        verify(buyerObj, times(1)).setTotalMoney(10, false);
        verify(fiefdom, times(1)).setLoyalVassals(10, true);
        verifyStatic(times(1));
        System.out.println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom() {
        Iterator iterator = Mockito.mock(Iterator.class);
        Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        Knight buyerObj = Mockito.mock(Knight.class);
        Map.map.put("fiefdomName", fiefdom);
        CurrentPlayers.currentPlayers.put("buyerName", buyerObj);
        when(iterator.next()).thenReturn("10").thenReturn("buyerName").thenReturn("fiefdomName");
        when(fiefdom.getOwner()).thenReturn("ownerName");
        when(buyerObj.getRank()).thenReturn("king");
        purchase.castleFiefdom(iterator);
        verify(fiefdom, times(1)).getOwner();
        verify(buyerObj, times(1)).getRank();
        verify(buyerObj, times(1)).setTotalMoney(250, false);
        verify(fiefdom, times(1)).buyFiefdomCastles(10);
        verifyStatic(times(1));
        System.out.println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal() {
        Iterator iterator = Mockito.mock(Iterator.class);
        Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        Knight buyerObj = Mockito.mock(Knight.class);
        Map.map.put("fiefdomName", fiefdom);
        CurrentPlayers.currentPlayers.put("buyerName", buyerObj);
        when(iterator.next()).thenReturn("10").thenReturn("buyerName").thenReturn("fiefdomName");
        when(fiefdom.getOwner()).thenReturn("ownerName");
        when(buyerObj.getRank()).thenReturn("king");
        purchase.castlePersonal(iterator);
        verify(fiefdom, times(1)).getOwner();
        verify(buyerObj, times(1)).getRank();
        verify(buyerObj, times(1)).setTotalMoney(100, false);
        verify(fiefdom, times(1)).buyPersonalCastles(10);
        verifyStatic(times(1));
        System.out.println("You cannot buy more Personal castle, you have insufficient funds");
    }

    @Test
    public void testMercenary() {
        Iterator iterator = Mockito.mock(Iterator.class);
        Knight knight = Mockito.mock(Knight.class);
        Knight buyerObj = Mockito.mock(Knight.class);
        CurrentPlayers.currentPlayers.put("buyerName", buyerObj);
        CurrentPlayers.currentPlayers.put("knightName", knight);
        when(iterator.next()).thenReturn("10").thenReturn("buyerName").thenReturn("knightName");
        when(buyerObj.getName()).thenReturn("buyerName");
        when(knight.getName()).thenReturn("knightName");
        when(buyerObj.getRank()).thenReturn("king");
        purchase.mercenary(iterator);
        verify(buyerObj, times(1)).getName();
        verify(knight, times(1)).getName();
        verify(buyerObj, times(1)).getRank();
        verify(buyerObj, times(1)).setTotalMoney(10, false);
        verify(knight, times(1)).addMercenaries(10);
        verifyStatic(times(1));
        System.out.println("You cannot buy more mercenaries, you have insufficient funds");
    }
}