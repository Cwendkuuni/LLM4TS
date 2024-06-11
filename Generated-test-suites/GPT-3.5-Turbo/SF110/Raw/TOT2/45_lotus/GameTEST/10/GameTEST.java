import static org.junit.Assert.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getPlayingPlayer());
        
        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when current player is player1
        Game.playingPlayer = 1;
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        
        // Test when current player is player2
        Game.playingPlayer = 2;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        // Test when player1 is passed as input
        assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
        
        // Test when player2 is passed as input
        assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
    }

    @Test
    public void testInit() {
        // Test initialization of players
        UserInterface player1UI = new UserInterface();
        UserInterface player2UI = new UserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(player1UI, player2UI, name1, name2, deck1, deck2);
        
        assertEquals(player1UI, Game.player1.getUserInterface());
        assertEquals(player2UI, Game.player2.getUserInterface());
        assertEquals(name1, Game.player1.getName());
        assertEquals(name2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test priority given to current player
        // This method is more complex and will need thorough testing
        // including edge cases and error scenarios, such as stack being empty
    }
}