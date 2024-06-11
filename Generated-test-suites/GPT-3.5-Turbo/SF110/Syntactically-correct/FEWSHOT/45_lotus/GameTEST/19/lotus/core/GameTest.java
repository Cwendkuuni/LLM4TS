package lotus.core;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.stack.Stack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    
    private UserInterface IP1;
    private UserInterface IP2;
    private String n1;
    private String n2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setup() {
        IP1 = new UserInterface();
        IP2 = new UserInterface();
        n1 = "Player1";
        n2 = "Player2";
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }

    @Test
    public void testGetPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player result = Game.getPlayingPlayer();
        assertEquals(Game.player1, result);

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        result = Game.getPlayingPlayer();
        assertEquals(Game.player2, result);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Test when playingPlayer is 1
        Game.playingPlayer = 1;
        Player result = Game.getNonPlayingPlayer();
        assertEquals(Game.player2, result);

        // Test when playingPlayer is 2
        Game.playingPlayer = 2;
        result = Game.getNonPlayingPlayer();
        assertEquals(Game.player1, result);
    }

    @Test
    public void testGetOtherPlayer() {
        // Test when p is Game.player1
        Player p = Game.player1;
        Player result = Game.getOtherPlayer(p);
        assertEquals(Game.player2, result);

        // Test when p is Game.player2
        p = Game.player2;
        result = Game.getOtherPlayer(p);
        assertEquals(Game.player1, result);
    }

    @Test
    public void testInit() {
        // Initialize game
        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        // Check if players are initialized correctly
        assertEquals(Game.player1, IP1.getPlayer());
        assertEquals(Game.player2, IP2.getPlayer());
        assertEquals(n1, IP1.getPlayer().getName());
        assertEquals(n2, IP2.getPlayer().getName());

        // Check if playingPlayer is set to 1
        assertEquals(1, Game.playingPlayer);

        // Check if currentPhase is an instance of UntapPhase
        assertTrue(Game.currentPhase instanceof UntapPhase);

        // Check if cards in deck1 and deck2 are assigned to respective players and zones
        for (Card c : deck1) {
            assertEquals(Game.player1, c.owner);
            assertEquals(Game.player1.library, c.zone);
        }
        for (Card c : deck2) {
            assertEquals(Game.player2, c.owner);
            assertEquals(Game.player2.library, c.zone);
        }
    }

    // Test givePriorityToCurrentPlayer() method

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Create a stack with one card
        Stack stack = new Stack();
        Card card = new Card();
        stack.push(card);
        Game.stack = stack;

        // Set playingPlayer to 1
        Game.playingPlayer = 1;

        // Mock the letPlayerSpeak() method to return true
        Game.player1 = new Player(IP1, n1, deck1) {
            @Override
            public boolean letPlayerSpeak() {
                return true;
            }
        };

        // Call the method
        Game.givePriorityToCurrentPlayer();

        // Check if stack is resolved
        assertTrue(Game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_StackNotEmpty() {
        // Create a stack with one card
        Stack stack = new Stack();
        Card card = new Card();
        stack.push(card);
        Game.stack = stack;

        // Set playingPlayer to 1
        Game.playingPlayer = 1;

        // Mock the letPlayerSpeak() method to return true
        Game.player1 = new Player(IP1, n1, deck1) {
            boolean called = false;

            @Override
            public boolean letPlayerSpeak() {
                if (!called) {
                    called = true;
                    // Push another card to the stack
                    Game.stack.push(new Card());
                    return false;
                }
                return true;
            }
        };

        // Call the method
        Game.givePriorityToCurrentPlayer();

        // Check if stack is resolved and method is called recursively
        assertTrue(Game.stack.empty());
        assertTrue(((Game.PlayerMock) Game.player1).called);
    }

    @SuppressWarnings("serial")
    private static class PlayerMock extends Player {
        private boolean called;

        public PlayerMock(UserInterface userInterface, String name, CardCollection deck) {
            super(userInterface, name, deck);
            called = false;
        }

        @Override
        public boolean letPlayerSpeak() {
            called = true;
            return true;
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer_BothPlayersDone() {
        // Create a stack with one card
        Stack stack = new Stack();
        Card card = new Card();
        stack.push(card);
        Game.stack = stack;

        // Set playingPlayer to 1
        Game.playingPlayer = 1;

        // Mock the letPlayerSpeak() method to return true
        Game.player1 = new PlayerMock(IP1, n1, deck1);
        Game.player2 = new PlayerMock(IP2, n2, deck2);

        // Call the method
        Game.givePriorityToCurrentPlayer();

        // Check if stack is resolved and method is called recursively
        assertTrue(Game.stack.empty());
        assertTrue(((Game.PlayerMock) Game.player1).called);
        assertTrue(((Game.PlayerMock) Game.player2).called);
    }

    @Test
    public void testGivePriorityToCurrentPlayer_StackEmpty() {
        // Set playingPlayer to 1
        Game.playingPlayer = 1;

        // Mock the letPlayerSpeak() method to return true
        Game.player1 = new Player(IP1, n1, deck1) {
            @Override
            public boolean letPlayerSpeak() {
                return true;
            }
        };

        // Call the method
        Game.givePriorityToCurrentPlayer();

        // Check if stack is empty and method returns without recursion
        assertTrue(Game.stack.empty());
    }

}