package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
	
	private ClientGroup clientGroup;
	
	@Before
	public void setup() {
		clientGroup = new ClientGroup(null);
	}
	
	@Test
	public void testParentGroup() {
		assertEquals(null, clientGroup.parentGroup());
	}
	
	@Test
	public void testNotifyMessageSent_NotInClients() {
		assertFalse(clientGroup.notifyMessageSent(null, null));
	}
	
	@Test
	public void testNotifyMessageSent_InSubGroup() {
		ClientGroup subGroup = clientGroup.createSubGroup();
		subGroup.add(new Client());
		assertTrue(clientGroup.notifyMessageSent(new Client(), null));
	}
	
	@Test
	public void testNotifyMessageSent_NoSubGroupNoHandler() {
		clientGroup.add(new Client());
		assertFalse(clientGroup.notifyMessageSent(new Client(), null));
	}
	
	@Test
	public void testNotifyMessageSent_NoSubGroupWithHandler() {
		clientGroup.add(new Client());
		clientGroup.addClientHandler(new ClientHandler());
		assertTrue(clientGroup.notifyMessageSent(new Client(), null));
	}
	
	@Test
	public void testNotifyMessageReceived_NotInClients() {
		assertFalse(clientGroup.notifyMessageReceived(null, null));
	}
	
	@Test
	public void testNotifyMessageReceived_InSubGroup() {
		ClientGroup subGroup = clientGroup.createSubGroup();
		subGroup.add(new Client());
		assertTrue(clientGroup.notifyMessageReceived(new Client(), null));
	}
	
	@Test
	public void testNotifyMessageReceived_NoSubGroupNoHandler() {
		clientGroup.add(new Client());
		assertFalse(clientGroup.notifyMessageReceived(new Client(), null));
	}
	
	@Test
	public void testNotifyMessageReceived_NoSubGroupWithHandler() {
		clientGroup.add(new Client());
		clientGroup.addClientHandler(new ClientHandler());
		assertTrue(clientGroup.notifyMessageReceived(new Client(), null));
	}
	
	@Test
	public void testNotifyExceptionCaught_NotInClients() {
		assertFalse(clientGroup.notifyExceptionCaught(null, null));
	}
	
	@Test
	public void testNotifyExceptionCaught_InSubGroup() {
		ClientGroup subGroup = clientGroup.createSubGroup();
		subGroup.add(new Client());
		assertTrue(clientGroup.notifyExceptionCaught(new Client(), new Throwable()));
	}
	
	@Test
	public void testNotifyExceptionCaught_NoSubGroupNoHandler() {
		clientGroup.add(new Client());
		assertFalse(clientGroup.notifyExceptionCaught(new Client(), new Throwable()));
	}
	
	@Test
	public void testNotifyExceptionCaught_NoSubGroupWithHandler() {
		clientGroup.add(new Client());
		clientGroup.addClientHandler(new ClientHandler());
		assertTrue(clientGroup.notifyExceptionCaught(new Client(), new Throwable()));
	}
	
	@Test
	public void testIterator() {
		clientGroup.add(new Client());
		assertTrue(clientGroup.iterator().hasNext());
	}
	
	@Test
	public void testContains() {
		Client client = new Client();
		clientGroup.add(client);
		assertTrue(clientGroup.contains(client));
	}
	
	@Test
	public void testSize() {
		clientGroup.add(new Client());
		assertEquals(1, clientGroup.size());
	}
	
	@Test
	public void testAdd_ParentContainsClient() {
		Client client = new Client();
		assertTrue(clientGroup.add(client));
	}
	
	@Test
	public void testAdd_ParentDoesNotContainClient() {
		ClientGroup parentGroup = new ClientGroup(null);
		clientGroup = new ClientGroup(parentGroup);
		assertFalse(clientGroup.add(new Client()));
	}
	
	@Test
	public void testRemove_ClientIsMember() {
		Client client = new Client();
		clientGroup.add(client);
		assertTrue(clientGroup.remove(client));	
	}
	
	@Test
	public void testRemove_ClientIsNotMember() {
		assertFalse(clientGroup.remove(new Client()));	
	}
	
	@Test
	public void testCreateSubGroup() {
		clientGroup.createSubGroup();
		assertEquals(1, clientGroup.size());
	}
	
	@Test
	public void testAddClientHandler() {
		clientGroup.addClientHandler(new ClientHandler());
		assertEquals(true, clientGroup.iterator().hasNext());
	}
	
}