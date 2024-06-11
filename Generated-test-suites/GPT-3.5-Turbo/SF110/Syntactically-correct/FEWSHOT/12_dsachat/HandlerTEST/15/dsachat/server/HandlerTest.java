
package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dsachat.share.*;

public class HandlerTest {
	
	private Handler handler;
	private ConnectionEndpoint cl;

	@Before
	public void setUp() {
		handler = new Handler();
		cl = new ConnectionEndpoint();
	}

	@Test
	public void testAddConnection() {
		handler.addConnection(cl);
		assertEquals(cl, handler.clients.lastElement().ce);
		assertEquals("", handler.clients.lastElement().name);
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(cl);
		assertEquals(1, handler.clients.size());
		handler.removeConnection(cl);
		assertEquals(0, handler.clients.size());
	}

	@Test
	public void testHandleInputString() {
		InputEvent event = new InputEvent("message", cl);
		handler.handleInput(event);
		// assert appropriate action taken
	}

	@Test
	public void testHandleInputChallReq() {
		InputEvent event = new InputEvent(new ChallReq(), cl);
		handler.handleInput(event);
		// assert appropriate action taken
	}

	@Test
	public void testHandleInputHero() {
		InputEvent event = new InputEvent(new Hero(), cl);
		handler.handleInput(event);
		// assert appropriate action taken
	}

	@Test
	public void testHandleInputUserEvent() {
		InputEvent event = new InputEvent(new UserEvent(), cl);
		handler.handleInput(event);
		// assert appropriate action taken
	}

	@Test
	public void testSendHeros() {
		// set up clients with heroes
		// assert that heroes are properly sent
	}

	@Test
	public void testIsGmConnected() {
		// set up clients with different names
		// assert that method returns true when Gamemaster is connected, false otherwise
	}

	@Test
	public void testSendOneSystemMsg() {
		// assert that the appropriate system message is sent to the specified client
	}

	@Test
	public void testSendAllSystemMsg() {
		// assert that the appropriate system message is sent to all clients
	}

	@Test
	public void testWhisper() {
		// set up clients with different names
		// assert that the appropriate whisper message is sent to the specified client
	}

	@Test
	public void testRollDiceWithModifier() {
		InputEvent event = new InputEvent("1wd6+3", cl);
		handler.handleInput(event);
		// assert that the roll result is properly sent to all clients
	}

	@Test
	public void testRollDiceWithoutModifier() {
		InputEvent event = new InputEvent("2wd6", cl);
		handler.handleInput(event);
		// assert that the roll results are properly sent to all clients
	}

	@Test
	public void testSendToOneStr() {
		// set up clients with different names
		// assert that the appropriate message is sent to the specified client
	}

	@Test
	public void testUpdateHeroWhenNameIsNull() {
		// assert that the hero is properly updated and the name is set to the hero's name
	}

	@Test
	public void testUpdateHeroWhenNameIsNotNull() {
		// assert that the hero is properly updated and the name remains the same
	}

	@Test
	public void testSendAllStr() {
		// set up clients with different names
		// assert that the appropriate message is sent to all clients
	}

	@Test
	public void testSendAllObj() {
		// assert that the appropriate object is sent to all clients
	}

	@Test
	public void testGetNameWhenNameIsFound() {
		// set up clients with different names
		// assert that the name of the specified client is returned
	}

	@Test
	public void testGetNameWhenNameIsNotFound() {
		// assert that null is returned when the client name is not found
	}
}
