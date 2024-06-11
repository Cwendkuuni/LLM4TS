import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private static final UserInterface IP1 = mock(UserInterface.class);
    private static final UserInterface IP2 = mock(UserInterface.class);
    private static final String NAME1 = "Player1";
    private static final String NAME2 = "Player2";
    private static final CardCollection DECK1 = mock(CardCollection.class);
    private static final CardCollection DECK2 = mock(CardCollection.class);

    private Game game;

    @Before
    public void setup() {
        game = new Game();
    }

    @Test
    public void testGetPlayingPlayer() {
        game.player1 = mock(Player.class);
        game.player2 = mock(Player.class);
        game.playingPlayer = 1;

        assertSame(game.player1, game.getPlayingPlayer());
    }

    @Test
    public void testGetPlayingPlayerPlayer2() {
        game.player1 = mock(Player.class);
        game.player2 = mock(Player.class);
        game.playingPlayer = 2;

        assertSame(game.player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        game.player1 = mock(Player.class);
        game.player2 = mock(Player.class);
        game.playingPlayer = 1;

        assertSame(game.player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayerPlayer2() {
        game.player1 = mock(Player.class);
        game.player2 = mock(Player.class);
        game.playingPlayer = 2;

        assertSame(game.player1, game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        game.init(IP1, IP2, NAME1, NAME2, DECK1, DECK2);

        assertSame(IP1, game.player1.getInterface());
        assertSame(IP2, game.player2.getInterface());
        assertEquals(NAME1, game.player1.getName());
        assertEquals(NAME2, game.player2.getName());
        assertSame(DECK1, game.player1.getDeck());
        assertSame(DECK2, game.player2.getDeck());
        assertSame(game.player1, game.getPlayingPlayer());
        assertSame(UntapPhase.class, game.currentPhase.getClass());

        verify(DECK1, times(1)).getCards();
        verify(DECK2, times(1)).getCards();
        verifyZeroInteractions(IP1, IP2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        game.player1 = mock(Player.class);
        game.player2 = mock(Player.class);
        game.currentPhase = mock(Phase.class);

        game.givePriorityToCurrentPlayer();

        verify(game.player1, times(1)).letPlayerSpeak();
        verify(game.player2, times(1)).letPlayerSpeak();
        verify(game.currentPhase, times(1)).resolveLast();
        verify(game.player1, times(2)).letPlayerSpeak();
        verify(game.player2, times(2)).letPlayerSpeak();
        verifyNoMoreInteractions(game.player1, game.player2, game.currentPhase);
    }
}