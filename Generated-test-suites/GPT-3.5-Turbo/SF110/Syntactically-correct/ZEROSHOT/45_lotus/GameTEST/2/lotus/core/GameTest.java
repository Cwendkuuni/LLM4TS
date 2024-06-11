import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.card.*;
import lotus.core.stack.*;
import lotus.core.phases.*;
import java.util.*;

public class GameTest {

    private UserInterface mockUI1;
    private UserInterface mockUI2;
    private String name1;
    private String name2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setUp() {
        mockUI1 = mock(UserInterface.class);
        mockUI2 = mock(UserInterface.class);
        name1 = "Player1";
        name2 = "Player2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Game.player1 = new Player(mockUI1, name1, deck1);
        assertEquals(Game.player1, Game.getPlayingPlayer());

        // Test when playingPlayer is not 1
        Game.playingPlayer = 2;
        Game.player2 = new Player(mockUI2, name2, deck2);
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.player1 = new Player(mockUI1, name1, deck1);
        Game.player2 = new Player(mockUI2, name2, deck2);

        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());

        // Test when playingPlayer is not 1
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Game.player1 = new Player(mockUI1, name1, deck1);
        Game.player2 = new Player(mockUI2, name2, deck2);
        Player testPlayer1 = new Player(mockUI1, name1, deck1);
        Player testPlayer2 = new Player(mockUI2, name2, deck2);

        // Test when p is player1
        assertEquals(Game.player2, Game.getOtherPlayer(testPlayer1));

        // Test when p is player2
        assertEquals(Game.player1, Game.getOtherPlayer(testPlayer2));
    }

    @Test
    public void testInit() {
        UserInterface mockUI1 = mock(UserInterface.class);
        UserInterface mockUI2 = mock(UserInterface.class);
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = mock(CardCollection.class);
        CardCollection deck2 = mock(CardCollection.class);

        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);

        when(deck1.iterator()).thenReturn(Collections.<Card>emptyListIterator());
        when(deck2.iterator()).thenReturn(Collections.<Card>emptyListIterator());

        Game.init(mockUI1, mockUI2, name1, name2, deck1, deck2);

        verify(mockPlayer1).setName(name1);
        verify(mockPlayer2).setName(name2);
        verify(mockPlayer1).setDeck(deck1);
        verify(mockPlayer2).setDeck(deck2);

        verify(mockUI1).init(mockPlayer1);
        verify(mockUI2).init(mockPlayer2);

        assertEquals(mockPlayer1, Game.player1);
        assertEquals(mockPlayer2, Game.player2);
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);

        Game.player1 = mockPlayer1;
        Game.player2 = mockPlayer2;

        Stack mockStack = mock(Stack.class);
        Game.stack = mockStack;

        when(mockPlayer1.letPlayerSpeak()).thenReturn(true);
        when(mockPlayer2.letPlayerSpeak()).thenReturn(true);
        when(mockStack.empty()).thenReturn(false).thenReturn(true);

        Game.givePriorityToCurrentPlayer();

        verify(mockStack).resolveLast();
        verify(mockPlayer1, times(2)).letPlayerSpeak();
        verify(mockPlayer2, times(2)).letPlayerSpeak();

        verify(mockPlayer1, times(1)).letPlayerSpeak();
        verifyNoMoreInteractions(mockPlayer2);
        verifyNoMoreInteractions(mockStack);
    }

}