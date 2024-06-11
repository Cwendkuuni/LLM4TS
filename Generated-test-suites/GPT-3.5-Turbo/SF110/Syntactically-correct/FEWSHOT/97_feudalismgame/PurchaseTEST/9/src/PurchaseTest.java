package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseTest {

    @Mock
    private Iterator arg;
    @Mock
    private Fiefdoms fiefdom;
    @Mock
    private Knight buyerObj;
    @Mock
    private Map map;
    @Mock
    private CurrentPlayers currentPlayers;

    private Purchase purchase;

    @Before
    public void setup() {
        purchase = new Purchase();
    }

    @Test
    public void testPerform() throws Exception {
        Collection<String> args = Arrays.asList("vassal", "5", "buyerName", "fiefdomName");
        when(arg.next()).thenReturn("vassal", "5", "buyerName", "fiefdomName");
        when(fiefdom.getOwner()).thenReturn(mock(SomeClass.class));
        when(buyerObj.getRank()).thenReturn("king");
        when(buyerObj.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        doNothing().when(fiefdom).setLoyalVassals(anyInt(), anyBoolean());
        purchase.perform(args);
        verify(fiefdom, times(1)).setLoyalVassals(anyInt(), anyBoolean());
    }

    @Test
    public void testVassal() throws Exception {
        when(arg.next()).thenReturn("5", "buyerName", "fiefdomName");
        when(fiefdom.getOwner()).thenReturn(mock(SomeClass.class));
        when(buyerObj.getRank()).thenReturn("king");
        when(buyerObj.isFiefdomMine(anyString())).thenReturn(true);
        when(buyerObj.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        purchase.vassal(arg);
        verify(fiefdom, times(1)).setLoyalVassals(anyInt(), anyBoolean());
    }

    @Test
    public void testCastleFiefdom() throws Exception {
        when(arg.next()).thenReturn("5", "buyerName", "fiefdomName");
        when(fiefdom.getOwner()).thenReturn(mock(SomeClass.class));
        when(buyerObj.getRank()).thenReturn("king");
        when(buyerObj.isFiefdomMine(anyString())).thenReturn(true);
        when(buyerObj.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        purchase.castleFiefdom(arg);
        verify(fiefdom, times(1)).buyFiefdomCastles(anyInt());
    }

    @Test
    public void testCastlePersonal() throws Exception {
        when(arg.next()).thenReturn("5", "buyerName", "fiefdomName");
        when(fiefdom.getOwner()).thenReturn(mock(SomeClass.class));
        when(buyerObj.getRank()).thenReturn("king");
        when(buyerObj.isFiefdomMine(anyString())).thenReturn(true);
        when(buyerObj.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        purchase.castlePersonal(arg);
        verify(fiefdom, times(1)).buyPersonalCastles(anyInt());
    }

    @Test
    public void testMercenary() throws Exception {
        when(arg.next()).thenReturn("5", "buyerName", "knightName");
        when(buyerObj.getName()).thenReturn("buyerName");
        when(buyerObj.getRank()).thenReturn("king");
        when(buyerObj.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        when(currentPlayers.containsKey(anyString())).thenReturn(true);
        when(currentPlayers.get(anyString())).thenReturn(mock(SomeClass.class));
        when(arg.next()).thenReturn("knightName");
        purchase.mercenary(arg);
        verify(buyerObj, times(1)).addMercenaries(anyInt());
    }
}