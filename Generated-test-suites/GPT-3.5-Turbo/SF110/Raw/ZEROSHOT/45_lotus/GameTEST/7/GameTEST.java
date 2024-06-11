import static org.junit.Assert.*;

import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playing player is 1
        Game.playingPlayer = 1;
        Player player1 = new Player();
        Game.player1 = player1;
        assertEquals(player1, Game.getPlayingPlayer());

        // Test when playing player is 2
        Game.playingPlayer = 2;
        Player player2 = new Player();
        Game.player2 = player2;
        assertEquals(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playing player is 1
        Game.playingPlayer = 1;
        Player player2 = new Player();
        Game.player2 = player2;
        assertEquals(player2, Game.getNonPlayingPlayer());

        // Test when playing player is 2
        Game.playingPlayer = 2;
        Player player1 = new Player();
        Game.player1 = player1;
        assertEquals(player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();
        Game.player1 = player1;
        Game.player2 = player2;

        // Test with player1
        assertEquals(player2, Game.getOtherPlayer(player1));

        // Test with player2
        assertEquals(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "Player 1";
        String n2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertEquals(IP1, Game.player1.getUI());
        assertEquals(IP2, Game.player2.getUI());
        assertEquals(n1, Game.player1.getName());
        assertEquals(n2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertEquals(1, Game.playingPlayer);
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // TODO: Add test case for givePriorityToCurrentPlayer() method
    }

}