
package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class PurchaseTest {
    private Purchase purchase;
    private Fiefdoms mockedFiefdom;
    private Knight mockedKnight;
    
    @Before
    public void setUp() {
        purchase = new Purchase();
        mockedFiefdom = mock(Fiefdoms.class);
        mockedKnight = mock(Knight.class);
    }

    @Test
    public void testVassal() {
        Iterator<String> args = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        CurrentPlayers.currentPlayers.put("buyerName", mockedKnight);
        Map.map.put("fiefdomName", mockedFiefdom);
        
        when(mockedFiefdom.getOwner()).thenReturn("ownerName");
        when(mockedKnight.getRank()).thenReturn("king");
        when(mockedKnight.isFiefdomMine(anyString())).thenReturn(true);
        when(mockedKnight.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        
        purchase.vassal(args);
        
        verify(mockedFiefdom).setLoyalVassals(eq(5), eq(true));
    }

    @Test
    public void testCastleFiefdom() {
        Iterator<String> args = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        CurrentPlayers.currentPlayers.put("buyerName", mockedKnight);
        Map.map.put("fiefdomName", mockedFiefdom);
        
        when(mockedFiefdom.getOwner()).thenReturn("ownerName");
        when(mockedKnight.getRank()).thenReturn("king");
        when(mockedKnight.isFiefdomMine(anyString())).thenReturn(true);
        when(mockedKnight.setTotalMoney(eq(5 * 25), anyBoolean())).thenReturn(true);
        
        purchase.castleFiefdom(args);
        
        verify(mockedFiefdom).buyFiefdomCastles(eq(5));
    }
    
    @Test
    public void testCastlePersonal() {
        Iterator<String> args = Arrays.asList("5", "buyerName", "fiefdomName").iterator();
        CurrentPlayers.currentPlayers.put("buyerName", mockedKnight);
        Map.map.put("fiefdomName", mockedFiefdom);
        
        when(mockedFiefdom.getOwner()).thenReturn("ownerName");
        when(mockedKnight.getRank()).thenReturn("king", "knight");
        when(mockedKnight.isFiefdomMine(anyString())).thenReturn(true);
        when(mockedKnight.setTotalMoney(eq(5 * 10), anyBoolean())).thenReturn(true);
        
        purchase.castlePersonal(args);
        
        verify(mockedFiefdom).buyPersonalCastles(eq(5));
    }
    
    @Test
    public void testMercenary() {
        Iterator<String> args = Arrays.asList("5", "buyerName", "knightName").iterator();
        CurrentPlayers.currentPlayers.put("buyerName", mockedKnight);
        CurrentPlayers.currentPlayers.put("knightName", mockedKnight);
        
        when(mockedKnight.getName()).thenReturn("knightName");
        when(mockedKnight.getRank()).thenReturn("king");
        when(mockedKnight.setTotalMoney(anyInt(), anyBoolean())).thenReturn(true);
        
        purchase.mercenary(args);
        
        verify(mockedKnight).addMercenaries(eq(5));
    }
}
