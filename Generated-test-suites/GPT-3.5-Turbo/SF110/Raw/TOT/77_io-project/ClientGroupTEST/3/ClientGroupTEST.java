
package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private ClientGroup parentGroup;
    private Client client;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        clientGroup = new ClientGroup(parentGroup);
        client = new Client();
    }

    // Test case for ClientGroup constructor
    @Test
    public void testConstructor() {
        assertNull(clientGroup.parentGroup());
        assertEquals(0, clientGroup.size());
    }

    // Test case for notifyMessageSent method - client is present in the group
    @Test
    public void testNotifyMessageSent_ClientPresent() {
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.notifyMessageSent(client, "test message"));
    }

    // Test case for notifyMessageSent method - client is not present in the group
    @Test
    public void testNotifyMessageSent_ClientNotPresent() {
        assertFalse(clientGroup.notifyMessageSent(client, "test message"));
    }

    // Test case for notifyMessageReceived method - client is present in the group
    @Test
    public void testNotifyMessageReceived_ClientPresent() {
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.notifyMessageReceived(client, "test message"));
    }

    // Test case for notifyMessageReceived method - client is not present in the group
    @Test
    public void testNotifyMessageReceived_ClientNotPresent() {
        assertFalse(clientGroup.notifyMessageReceived(client, "test message"));
    }

    // Test case for notifyExceptionCaught method - client is present in the group
    @Test
    public void testNotifyExceptionCaught_ClientPresent() {
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    // Test case for notifyExceptionCaught method - client is not present in the group
    @Test
    public void testNotifyExceptionCaught_ClientNotPresent() {
        assertFalse(clientGroup.notifyExceptionCaught(client, new Throwable()));
    }

    // Test case for iterator method
    @Test
    public void testIterator() {
        assertEquals(0, clientGroup.size());
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        assertEquals(2, clientGroup.size());
    }

    // Test case for contains method - client is present in the group
    @Test
    public void testContains_ClientPresent() {
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
    }

    // Test case for contains method - client is not present in the group
    @Test
    public void testContains_ClientNotPresent() {
        assertFalse(clientGroup.contains(client));
    }

    // Test case for size method
    @Test
    public void testSize() {
        assertEquals(0, clientGroup.size());
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        assertEquals(2, clientGroup.size());
    }

    // Test case for add method - parent group contains the client
    @Test
    public void testAdd_ParentGroupContainsClient() {
        assertTrue(parentGroup.add(client));
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.contains(client));
    }

    // Test case for add method - parent group does not contain the client
    @Test
    public void testAdd_ParentGroupDoesNotContainClient() {
        assertFalse(parentGroup.contains(client));
        assertFalse(clientGroup.add(client));
        assertFalse(clientGroup.contains(client));
    }

    // Test case for remove method - client is present in the group
    @Test
    public void testRemove_ClientPresent() {
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
    }

    // Test case for remove method - client is not present in the group
    @Test
    public void testRemove_ClientNotPresent() {
        assertFalse(clientGroup.remove(client));
    }

    // Test case for createSubGroup method
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertNotNull(subGroup);
        assertSame(clientGroup, subGroup.parentGroup());
    }

    // Test case for addClientHandler method
    @Test
    public void testAddClientHandler() {
        assertNull(clientGroup.handler);
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        assertNotNull(clientGroup.handler);
        assertSame(handler, clientGroup.handler);
    }
}
