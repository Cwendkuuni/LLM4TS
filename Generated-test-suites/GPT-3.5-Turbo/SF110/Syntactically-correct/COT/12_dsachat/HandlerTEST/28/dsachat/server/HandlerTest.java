import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HandlerTest {
	private Handler handler;

	@Before
	public void setup() {
		handler = new Handler();
	}

	@Test
	public void testAddConnection() {
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		handler.addConnection(cl);

		Vector<Entry> clients = handler.getClients();
		assertEquals(1, clients.size());
		assertEquals(cl, clients.get(0).ce);
		assertEquals("", clients.get(0).name);
	}

	@Test
	public void testRemoveConnection() {
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		handler.addConnection(cl);
		handler.removeConnection(cl);

		Vector<Entry> clients = handler.getClients();
		assertEquals(0, clients.size());
	}

	@Test
	public void testHandleInput_StringCommandDice() {
		String command = "/dice 6";
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		ChallengeMock challenge = new ChallengeMock();
		InputEventMock event = new InputEventMock(command, cl);

		handler.handleInput(event);

		assertEquals("You roll: 6", cl.getLastMessage());
		assertEquals(challenge.getTo(), null);
	}

	@Test
	public void testHandleInput_StringCommandWhisper() {
		String command = "/whisper User1 Hello!";
		ConnectionEndpointMock cl1 = new ConnectionEndpointMock(1);
		ConnectionEndpointMock cl2 = new ConnectionEndpointMock(2);
		handler.addConnection(cl1);
		handler.addConnection(cl2);
		InputEventMock event = new InputEventMock(command, cl1);

		handler.handleInput(event);

		assertEquals("You whisper to User1: Hello!", cl1.getLastMessage());
		assertEquals("User1 whispers: Hello!", cl2.getLastMessage());
	}

	@Test
	public void testHandleInput_StringCommandMe() {
		String command = "Hello!";
		ConnectionEndpointMock cl1 = new ConnectionEndpointMock(1);
		ConnectionEndpointMock cl2 = new ConnectionEndpointMock(2);
		handler.addConnection(cl1);
		handler.addConnection(cl2);
		InputEventMock event = new InputEventMock(command, cl1);

		handler.handleInput(event);

		assertEquals("You whisper to User1: Hello!", cl1.getLastMessage());
		assertEquals("User1 whispers: Hello!", cl2.getLastMessage());
	}

	@Test
	public void testHandleInput_StringCommandUnrecognized() {
		String command = "/unknown Hello!";
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		handler.addConnection(cl);
		InputEventMock event = new InputEventMock(command, cl);

		handler.handleInput(event);

		assertEquals("command /unknown not found", cl.getLastMessage());
	}

	@Test
	public void testHandleInput_Challenge() {
		Challenge challenge = new Challenge();
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		InputEventMock event = new InputEventMock(challenge, cl);

		handler.handleInput(event);

		assertEquals("You roll: 10", cl.getLastMessage());
		assertEquals(challenge.getTo(), null);
	}

	@Test
	public void testHandleInput_ChallReq() {
		ChallReq challReq = new ChallReq();
		ConnectionEndpointMock cl1 = new ConnectionEndpointMock(1);
		ConnectionEndpointMock cl2 = new ConnectionEndpointMock(2);
		handler.addConnection(cl1);
		handler.addConnection(cl2);
		InputEventMock event = new InputEventMock(challReq, cl1);

		handler.handleInput(event);

		assertTrue(cl2.getLastMessage() instanceof ChallReq);
	}

	@Test
	public void testHandleInput_Hero() {
		Hero hero = new Hero();
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		InputEventMock event = new InputEventMock(hero, cl);

		handler.handleInput(event);

		assertEquals("Welcome HeroName", cl.getLastMessage());
	}

	@Test
	public void testHandleInput_UserEvent() {
		EntryMock entry = new EntryMock();
		UserEvent userEvent = new UserEvent(false, "User1");
		ConnectionEndpointMock cl = new ConnectionEndpointMock();
		handler.addConnection(cl);
		InputEventMock event = new InputEventMock(userEvent, cl);

		handler.handleInput(event);

		assertEquals("Welcome User1", cl.getLastMessage());
		assertEquals("[UserEvent] false, User1", entry.getEvents().toString());
	}

	private class ConnectionEndpointMock extends ConnectionEndpoint {
		private String lastMessage;

		public ConnectionEndpointMock() {
			super(null);
		}

		public ConnectionEndpointMock(int clientPort) {
			super(clientPort);
		}

		@Override
		public void send(Object o) throws IOException {
			if (o instanceof String) {
				lastMessage = (String) o;
			}
		}

		public String getLastMessage() {
			return lastMessage;
		}
	}

	private class ChallengeMock extends Challenge {
		@Override
		public String roll() {
			return "You roll: 10";
		}
	}

	private class InputEventMock extends InputEvent {
		public InputEventMock(Object data, ConnectionEndpoint source) {
			super(data, source);
		}
	}

	private class EntryMock extends Entry {
		private Vector<Object> events = new Vector<>();

		@Override
		public void send(Object o) throws IOException {
			events.add(o);
		}

		public Vector<Object> getEvents() {
			return events;
		}
	}
}