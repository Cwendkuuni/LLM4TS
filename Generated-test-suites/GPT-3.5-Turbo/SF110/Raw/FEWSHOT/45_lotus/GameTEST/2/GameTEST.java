import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import lotus.core.*;
import lotus.core.card.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.stack.*;

public class GameTest {
	
	private UserInterfaceMock ui;
	private CardCollection deck1;
	private CardCollection deck2;
	
	@Before
	public void setup() {
		ui = new UserInterfaceMock();
		deck1 = new CardCollection();
		deck2 = new CardCollection();
	}
	
	@Test
	public void testGetPlayingPlayer() {
		Game.player1 = new Player(ui, "Player1", deck1);
		Game.player2 = new Player(ui, "Player2", deck2);
		Game.playingPlayer = 1;
		
		Player playingPlayer = Game.getPlayingPlayer();
		
		assertEquals(Game.player1, playingPlayer);
	}
	
	@Test
	public void testGetNonPlayingPlayer() {
		Game.player1 = new Player(ui, "Player1", deck1);
		Game.player2 = new Player(ui, "Player2", deck2);
		Game.playingPlayer = 1;
		
		Player nonPlayingPlayer = Game.getNonPlayingPlayer();
		
		assertEquals(Game.player2, nonPlayingPlayer);
	}
	
	@Test
	public void testGetOtherPlayer() {
		Game.player1 = new Player(ui, "Player1", deck1);
		Game.player2 = new Player(ui, "Player2", deck2);
		
		Player otherPlayer1 = Game.getOtherPlayer(Game.player1);
		Player otherPlayer2 = Game.getOtherPlayer(Game.player2);
		
		assertEquals(Game.player2, otherPlayer1);
		assertEquals(Game.player1, otherPlayer2);
	}
	
	@Test
	public void testInit() {
		Game.player1 = null;
		Game.player2 = null;
		ui.initCalled = false;
		
		CardCollection deck1 = new CardCollection();
		CardCollection deck2 = new CardCollection();
		
		Game.init(ui, ui, "Player1", "Player2", deck1, deck2);
		
		assertEquals("Player1", Game.player1.getName());
		assertEquals("Player2", Game.player2.getName());
		assertEquals(deck1, Game.player1.getDeck());
		assertEquals(deck2, Game.player2.getDeck());
		assertEquals(Game.player1, deck1.get(0).getOwner());
		assertEquals(Game.player2, deck2.get(0).getOwner());
		assertEquals(Game.player1.library, deck1.get(0).getZone());
		assertEquals(Game.player2.library, deck2.get(0).getZone());
		assertEquals(true, ui.initCalled);
		assertEquals(Game.player1, ui.initPlayer);
		assertEquals(Game.player2, ui.initPlayer);
	}
	
	@Test
	public void testGivePriorityToCurrentPlayer() {
		Game.stack.push(new TestStackObject());
		
		Game.givePriorityToCurrentPlayer();
		
		assertEquals(false, Game.stack.isEmpty());
		assertEquals(true, Game.stack.isResolved);
		
		Game.stack.pop();
		
		Game.givePriorityToCurrentPlayer();
		
		assertEquals(true, Game.stack.isEmpty());
	}
	
	private class UserInterfaceMock implements UserInterface {
		public boolean initCalled = false;
		public Player initPlayer;
		
		@Override
		public void init(Player player) {
			initCalled = true;
			initPlayer = player;
		}
		
		@Override
		public boolean letPlayerSpeak() {
			return true;
		}
	}
	
	private class TestStackObject implements StackObject {
		public boolean isResolved = false;
		
		@Override
		public void resolve() {
			isResolved = true;
		}
	}
}