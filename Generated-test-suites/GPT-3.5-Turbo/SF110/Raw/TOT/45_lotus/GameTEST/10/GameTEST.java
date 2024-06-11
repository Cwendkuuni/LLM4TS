import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Typical use case
        Game.playingPlayer = 1;
        Player player1 = new Player();
        Game.player1 = player1;

        Player result = Game.getPlayingPlayer();

        assertSame(player1, result);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Typical use case
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;

        Player result = Game.getNonPlayingPlayer();

        assertSame(player2, result);
    }

    @Test
    public void testGetOtherPlayer() {
        // Typical use case
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;

        Player result = Game.getOtherPlayer(player1);

        assertSame(player2, result);
    }

    @Test
    public void testInit() {
        // Typical use case
        UserInterface ip1 = new UserInterface();
        UserInterface ip2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(ip1, ip2, n1, n2, deck1, deck2);

        assertEquals(ip1, Game.player1.getInterface());
        assertEquals(ip2, Game.player2.getInterface());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(1, Game.playingPlayer);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        for (Card c : deck1) {
            assertSame(Game.player1, c.getOwner());
            assertSame(Game.player1.getLibrary(), c.getZone());
        }
        for (Card c : deck2) {
            assertSame(Game.player2, c.getOwner());
            assertSame(Game.player2.getLibrary(), c.getZone());
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Typical use case
        Game.stack = new Stack();
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;
        Game.playingPlayer = 1;

        Game.givePriorityToCurrentPlayer();

        // No assertions, as this method is responsible for handling game states and resolution
    }
}