import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;

public class GameTest {

    @Mock
    UserInterface userInterface1;
    
    @Mock
    UserInterface userInterface2;
    
    @Mock
    CardCollection deck1;
    
    @Mock
    CardCollection deck2;

    @Before
    public void setUp() {
        userInterface1 = mock(UserInterface.class);
        userInterface2 = mock(UserInterface.class);
        deck1 = mock(CardCollection.class);
        deck2 = mock(CardCollection.class);
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = Mockito.mock(Player.class);
        Game.player2 = Mockito.mock(Player.class);
        Game.playingPlayer = 1;

        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        Game.playingPlayer = 2;

        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = Mockito.mock(Player.class);
        Game.player2 = Mockito.mock(Player.class);

        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Game.player1 = Mockito.mock(Player.class);
        Game.player2 = Mockito.mock(Player.class);
        Player player = Mockito.mock(Player.class);

        player = Game.getOtherPlayer(Game.player1);

        assertEquals(Game.player2, player);
        
        player = Game.getOtherPlayer(Game.player2);

        assertEquals(Game.player1, player);
    }

    @Test
    public void testInit() {
        Game.player1 = Mockito.mock(Player.class);
        Game.player2 = Mockito.mock(Player.class);

        Game.init(userInterface1, userInterface2, "n1", "n2", deck1, deck2);

        verify(Game.player1).init(userInterface1);
        verify(Game.player2).init(userInterface2);
        assertEquals(1, Game.playingPlayer);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        verify(deck1, atLeastOnce()).forEach(any());
        verify(deck2, atLeastOnce()).forEach(any());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = Mockito.mock(Stack.class);

        Game.givePriorityToCurrentPlayer();

        verify(Game.stack).empty();
        verify(Game.stack).resolveLast();
    }

}