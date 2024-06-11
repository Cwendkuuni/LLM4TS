package ioproject.server.network;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;

public class ClientGroupTest {

	private ClientGroup clientGroup;

	@Before
	public void setUp() {
		clientGroup = new ClientGroup(null);
	}

	@Test
	public void testParentGroup() {
		ClientGroup parentGroup = new ClientGroup(null);
		clientGroup = new ClientGroup(parentGroup);
		assertEquals(parentGroup, clientGroup.parentGroup());
	}

	@Test
	public void testNotifyMessageSent() {
		Client client = new Client();
		Object message = new Object();

		assertFalse(clientGroup.notifyMessageSent(client, message));

		clientGroup.add(client);
		assertTrue(clientGroup.notifyMessageSent(client, message));

		ClientGroup subGroup1 = clientGroup.createSubGroup();
		ClientGroup subGroup2 = clientGroup.createSubGroup();
		ClientGroup subGroup3 = clientGroup.createSubGroup();
		subGroup2.remove(client);

		assertTrue(clientGroup.notifyMessageSent(client, message));

		ClientHandler handler = new ClientHandler();
		clientGroup.addClientHandler(handler);
		clientGroup.notifyMessageSent(client, message);
		assertEquals(client, handler.messageSentClient);
		assertEquals(message, handler.messageSentMessage);
	}

	@Test
	public void testNotifyMessageReceived() {
		Client client = new Client();
		Object message = new Object();

		assertFalse(clientGroup.notifyMessageReceived(client, message));

		clientGroup.add(client);
		assertTrue(clientGroup.notifyMessageReceived(client, message));

		ClientGroup subGroup1 = clientGroup.createSubGroup();
		ClientGroup subGroup2 = clientGroup.createSubGroup();
		ClientGroup subGroup3 = clientGroup.createSubGroup();
		subGroup2.remove(client);

		assertTrue(clientGroup.notifyMessageReceived(client, message));

		ClientHandler handler = new ClientHandler();
		clientGroup.addClientHandler(handler);
		clientGroup.notifyMessageReceived(client, message);
		assertEquals(client, handler.messageReceivedClient);
		assertEquals(message, handler.messageReceivedMessage);
	}

	@Test
	public void testNotifyExceptionCaught() {
		Client client = new Client();
		Throwable cause = new Throwable();

		assertFalse(clientGroup.notifyExceptionCaught(client, cause));

		clientGroup.add(client);
		assertTrue(clientGroup.notifyExceptionCaught(client, cause));

		ClientGroup subGroup1 = clientGroup.createSubGroup();
		ClientGroup subGroup2 = clientGroup.createSubGroup();
		ClientGroup subGroup3 = clientGroup.createSubGroup();
		subGroup2.remove(client);

		assertTrue(clientGroup.notifyExceptionCaught(client, cause));

		ClientHandler handler = new ClientHandler();
		clientGroup.addClientHandler(handler);
		clientGroup.notifyExceptionCaught(client, cause);
		assertEquals(client, handler.exceptionCaughtClient);
		assertEquals(cause, handler.exceptionCaughtCause);
	}

	@Test
	public void testIterator() {
		Client client1 = new Client();
		Client client2 = new Client();
		clientGroup.add(client1);
		clientGroup.add(client2);

		assertTrue(clientGroup.iterator().hasNext());
		assertEquals(client1, clientGroup.iterator().next());
	}

	@Test
	public void testContains() {
		Client client = new Client();

		assertFalse(clientGroup.contains(client));

		clientGroup.add(client);
		assertTrue(clientGroup.contains(client));
	}

	@Test
	public void testSize() {
		Client client1 = new Client();
		Client client2 = new Client();

		assertEquals(0, clientGroup.size());

		clientGroup.add(client1);
		assertEquals(1, clientGroup.size());

		clientGroup.add(client2);
		assertEquals(2, clientGroup.size());
	}

	@Test
	public void testAdd() {
		Client client = new Client();

		assertFalse(clientGroup.add(client));

		ClientGroup parentGroup = new ClientGroup(null);
		clientGroup = new ClientGroup(parentGroup);
		assertTrue(clientGroup.add(client));
		assertTrue(clientGroup.contains(client));
	}

	@Test
	public void testRemove() {
		Client client = new Client();

		assertTrue(clientGroup.remove(client));

		clientGroup.add(client);
		assertTrue(clientGroup.remove(client));
		assertFalse(clientGroup.contains(client));

		ClientGroup subGroup = clientGroup.createSubGroup();
		subGroup.add(client);

		assertTrue(clientGroup.remove(client));
		assertFalse(clientGroup.contains(client));
		assertFalse(subGroup.contains(client));
	}

	@Test
	public void testCreateSubGroup() {
		ClientGroup subGroup = clientGroup.createSubGroup();
		assertNotNull(subGroup);
		assertTrue(clientGroup.iterator().hasNext());
		assertEquals(subGroup, clientGroup.iterator().next());
	}

	@Test
	public void testAddClientHandler() {
		ClientHandler handler = new ClientHandler();
		clientGroup.addClientHandler(handler);
		assertEquals(handler, clientGroup.handler);
	}

	private class ClientHandler {
		Client messageSentClient;
		Object messageSentMessage;
		Client messageReceivedClient;
		Object messageReceivedMessage;
		Client exceptionCaughtClient;
		Throwable exceptionCaughtCause;

		public void messageSent(Client client, Object message) {
			messageSentClient = client;
			messageSentMessage = message;
		}

		public void messageReceived(Client client, Object message) {
			messageReceivedClient = client;
			messageReceivedMessage = message;
		}

		public void exceptionCaught(Client client, Throwable cause) {
			exceptionCaughtClient = client;
			exceptionCaughtCause = cause;
		}

		public void clientAdded(Client client) {

		}

		public void clientRemoved(Client client) {

		}
	}
}