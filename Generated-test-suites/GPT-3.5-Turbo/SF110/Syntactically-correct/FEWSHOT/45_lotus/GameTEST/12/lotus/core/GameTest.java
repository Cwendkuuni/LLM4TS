package lotus.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import lotus.core.interfaces.UserInterface;
import lotus.core.card.CardCollection;
import lotus.core.card.Card;
import java.util.List;
import java.util.ArrayList;

public class GameTest {

    private UserInterface userInterface1;
    private UserInterface userInterface2;
    private String name1;
    private String name2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setUp() {
        userInterface1 = mock(UserInterface.class);
        userInterface2 = mock(UserInterface.class);
        name1 = "Player 1";
        name2 = "Player 2";
        deck1 = mock(CardCollection.class);
        deck2 = mock(CardCollection.class);
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.player1 = mock(Player.class);
        Game.player2 = mock(Player.class);
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = mock(Player.class);
        Game.player2 = mock(Player.class);
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Game.player1 = mock(Player.class);
        Game.player2 = mock(Player.class);
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        Game.player1 = mock(Player.class);
        Game.player2 = mock(Player.class);
        Game.currentPhase = mock(Phase.class);
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        List<Card> deck1List = new ArrayList<>();
        List<Card> deck2List = new ArrayList<>();
        deck1List.add(card1);
        deck2List.add(card2);
        when(deck1.iterator()).thenReturn(deck1List.iterator());
        when(deck2.iterator()).thenReturn(deck2List.iterator());

        Game.init(userInterface1, userInterface2, name1, name2, deck1, deck2);

        verify(Game.player1).init(userInterface1);
        verify(Game.player2).init(userInterface2);

        assertEquals(Game.player1, card1.owner);
        assertEquals(Game.player2, card2.owner);
        assertEquals(Game.player1.library, card1.zone);
        assertEquals(Game.player2.library, card2.zone);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack = mock(Stack.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game.player1 = player1;
        Game.player2 = player2;
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);
        
        Game.givePriorityToCurrentPlayer();
        
        verify(player1, times(1)).letPlayerSpeak();
        verify(player2, times(1)).letPlayerSpeak();
        verify(Game.stack).empty();
        verify(Game.stack, times(0)).resolveLast();

        reset(Game.stack);

        when(Game.stack.empty()).thenReturn(false);
        Game.givePriorityToCurrentPlayer();
        verify(Game.stack).resolveLast();

        reset(Game.stack);

        when(player1.letPlayerSpeak()).thenReturn(false);
        Game.givePriorityToCurrentPlayer();
        verify(player1, times(2)).letPlayerSpeak();
        verify(player2, times(2)).letPlayerSpeak();
    }
}