import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import lotus.core.UserInterface;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.interfaces.Player;

import org.junit.BeforeClass;
import org.junit.Test;

public class GameTest {
    
    private static UserInterface IP1;
    private static UserInterface IP2;
    private static String n1;
    private static String n2;
    private static CardCollection deck1;
    private static CardCollection deck2;

    @BeforeClass
    public static void setUp() {
        IP1 = mock(UserInterface.class);
        IP2 = mock(UserInterface.class);
        n1 = "Player 1";
        n2 = "Player 2";
        deck1 = mock(CardCollection.class);
        deck2 = mock(CardCollection.class);
    }
    
    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player(IP1, n1, deck1);
        Player player2 = new Player(IP2, n2, deck2);
        
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        
        assertSame(player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;
        
        assertSame(player2, Game.getPlayingPlayer());
    }
    
    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(IP1, n1, deck1);
        Player player2 = new Player(IP2, n2, deck2);
        
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;
        
        assertSame(player2, Game.getNonPlayingPlayer());
        
        Game.playingPlayer = 2;
        
        assertSame(player1, Game.getNonPlayingPlayer());
    }
    
    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(IP1, n1, deck1);
        Player player2 = new Player(IP2, n2, deck2);
        
        Game.player1 = player1;
        Game.player2 = player2;
        
        assertSame(player2, Game.getOtherPlayer(player1));
        assertSame(player1, Game.getOtherPlayer(player2));
    }
    
    @Test
    public void testInit() {
        Player player1 = new Player(IP1, n1, deck1);
        Player player2 = new Player(IP2, n2, deck2);
        
        Game.player1 = null;
        Game.player2 = null;
        
        Game.init(IP1, IP2, n1, n2, deck1, deck2);
        
        assertSame(player1, Game.player1);
        assertSame(player2, Game.player2);
        verify(IP1).init(player1);
        verify(IP2).init(player2);
        
        for (Card c : deck1) {
            assertSame(player1, c.owner);
            assertSame(player1.library, c.zone);
        }
        
        for (Card c : deck2) {
            assertSame(player2, c.owner);
            assertSame(player2.library, c.zone);
        }
    }
    
    @Test
    public void testGivePriorityToCurrentPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        
        Game.player1 = player1;
        Game.player2 = player2;
        Game.stack = mock(Stack.class);
        
        when(player1.letPlayerSpeak()).thenReturn(true, false);
        when(player2.letPlayerSpeak()).thenReturn(true, false);
        
        Game.givePriorityToCurrentPlayer();
        
        verify(player1, times(2)).letPlayerSpeak();
        verify(player2, times(2)).letPlayerSpeak();
        verify(Game.stack).resolveLast();
    }
}