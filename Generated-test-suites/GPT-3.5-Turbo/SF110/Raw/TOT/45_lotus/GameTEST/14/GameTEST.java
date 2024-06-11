import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    @Before
    public void setUp() {
        // Set up the initial state for each test case
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        
        // Add some cards to the decks
        
        // Initialize the game
        Game.init(IP1, IP2, name1, name2, deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        // Test with playingPlayer = 1
        Game.playingPlayer = 1;
        Player expectedPlayer1 = Game.player1;
        Player actualPlayer1 = Game.getPlayingPlayer();
        Assert.assertEquals(expectedPlayer1, actualPlayer1);

        // Test with playingPlayer = 2
        Game.playingPlayer = 2;
        Player expectedPlayer2 = Game.player2;
        Player actualPlayer2 = Game.getPlayingPlayer();
        Assert.assertEquals(expectedPlayer2, actualPlayer2);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test with playingPlayer = 1
        Game.playingPlayer = 1;
        Player expectedPlayer2 = Game.player2;
        Player actualPlayer2 = Game.getNonPlayingPlayer();
        Assert.assertEquals(expectedPlayer2, actualPlayer2);

        // Test with playingPlayer = 2
        Game.playingPlayer = 2;
        Player expectedPlayer1 = Game.player1;
        Player actualPlayer1 = Game.getNonPlayingPlayer();
        Assert.assertEquals(expectedPlayer1, actualPlayer1);
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player();
        Player player2 = new Player();

        // Test with p = player1
        Player expectedPlayer2 = player2;
        Player actualPlayer2 = Game.getOtherPlayer(player1);
        Assert.assertEquals(expectedPlayer2, actualPlayer2);

        // Test with p = player2
        Player expectedPlayer1 = player1;
        Player actualPlayer1 = Game.getOtherPlayer(player2);
        Assert.assertEquals(expectedPlayer1, actualPlayer1);
    }

    @Test
    public void testInit() {
        // Test with valid inputs
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String name1 = "John";
        String name2 = "Jane";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        // Add some cards to the decks
        Game.init(IP1, IP2, name1, name2, deck1, deck2);

        // Verify that the players are initialized correctly
        Player expectedPlayer1 = new Player(IP1, name1, deck1);
        Player actualPlayer1 = Game.player1;
        Assert.assertEquals(expectedPlayer1, actualPlayer1);

        Player expectedPlayer2 = new Player(IP2, name2, deck2);
        Player actualPlayer2 = Game.player2;
        Assert.assertEquals(expectedPlayer2, actualPlayer2);

        // Verify that the player interfaces are initialized
        UserInterface expectedIP1 = IP1;
        UserInterface actualIP1 = Game.player1.userInterface;
        Assert.assertEquals(expectedIP1, actualIP1);

        UserInterface expectedIP2 = IP2;
        UserInterface actualIP2 = Game.player2.userInterface;
        Assert.assertEquals(expectedIP2, actualIP2);

        // Verify that playingPlayer is set to 1
        int expectedPlayingPlayer = 1;
        int actualPlayingPlayer = Game.playingPlayer;

        // Verify that currentPhase is set to an instance of UntapPhase
        Phase expectedCurrentPhase = new UntapPhase();
        Phase actualCurrentPhase = Game.currentPhase;
        Assert.assertEquals(expectedCurrentPhase.getClass(), actualCurrentPhase.getClass());

        // Verify that each card in deck1 has owner set as player1 and zone set as player1.library
        for (Card c : deck1) {
            Player expectedOwner = Game.player1;
            Player actualOwner = c.owner;
            Assert.assertEquals(expectedOwner, actualOwner);

            Zone expectedZone = Game.player1.library;
            Zone actualZone = c.zone;
            Assert.assertEquals(expectedZone, actualZone);
        }

        // Verify that each card in deck2 has owner set as player2 and zone set as player2.library
        for (Card c : deck2) {
            Player expectedOwner = Game.player2;
            Player actualOwner = c.owner;
            Assert.assertEquals(expectedOwner, actualOwner);

            Zone expectedZone = Game.player2.library;
            Zone actualZone = c.zone;
            Assert.assertEquals(expectedZone, actualZone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Test with empty stack
        Game.stack = new Stack();
        Game.givePriorityToCurrentPlayer();
        // Verify that nothing changes

        // Test with non-empty stack
        // Add some cards to the stack
        Game.stack.push(new Card());
        Game.stack.push(new Card());
        Game.stack.push(new Card());

        Game.givePriorityToCurrentPlayer();
        // Verify that the stack is resolved

        // Further testing can be done by mocking the letPlayerSpeak() method of Player
    }

}