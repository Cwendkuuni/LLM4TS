package ioproject.server.network;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        // Create parent Group
        ClientGroup parentGroup = new ClientGroup(null);

        // Create child Group
        ClientGroup childGroup = new ClientGroup(parentGroup);

        // Check if parent group is set correctly
        assertEquals(parentGroup, childGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();

        // Add client to group
        parentGroup.add(client);

        // Set up client handler
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);

        // Test notifyMessageSent
        assertTrue(parentGroup.notifyMessageSent(client, message));
        assertNull(handler.lastMessageSent);

        // Create sub group
        ClientGroup subGroup = parentGroup.createSubGroup();

        // Add sub group handler
        ClientHandler subGroupHandler = new ClientHandler();
        subGroup.addClientHandler(subGroupHandler);

        // Test notifyMessageSent with sub group
        assertTrue(parentGroup.notifyMessageSent(client, message));
        assertNotNull(handler.lastMessageSent);
        assertNull(subGroupHandler.lastMessageReceived);
    }

    @Test
    public void testNotifyMessageReceived() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();

        // Add client to group
        parentGroup.add(client);

        // Set up client handler
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);

        // Test notifyMessageReceived
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        assertNull(handler.lastMessageReceived);

        // Create sub group
        ClientGroup subGroup = parentGroup.createSubGroup();

        // Add sub group handler
        ClientHandler subGroupHandler = new ClientHandler();
        subGroup.addClientHandler(subGroupHandler);

        // Test notifyMessageReceived with sub group
        assertTrue(parentGroup.notifyMessageReceived(client, message));
        assertNotNull(handler.lastMessageReceived);
        assertNull(subGroupHandler.lastMessageSent);
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new Throwable();

        // Add client to group
        parentGroup.add(client);

        // Set up client handler
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);

        // Test notifyExceptionCaught
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertNull(handler.lastExceptionCaught);

        // Create sub group
        ClientGroup subGroup = parentGroup.createSubGroup();

        // Add sub group handler
        ClientHandler subGroupHandler = new ClientHandler();
        subGroup.addClientHandler(subGroupHandler);

        // Test notifyExceptionCaught with sub group
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        assertNotNull(handler.lastExceptionCaught);
        assertNull(subGroupHandler.lastExceptionCaught);
    }

    @Test
    public void testIterator() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();

        // Add clients to group
        parentGroup.add(client1);
        parentGroup.add(client2);

        // Test iterator
        int count = 0;
        for (Client client : parentGroup) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testContains() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();

        // Add client to group
        parentGroup.add(client);

        // Test contains
        assertTrue(parentGroup.contains(client));
        assertFalse(parentGroup.contains(new Client()));
    }

    @Test
    public void testSize() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();

        // Add clients to group
        parentGroup.add(client1);
        parentGroup.add(client2);

        // Test size
        assertEquals(2, parentGroup.size());
    }

    @Test
    public void testAdd() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();

        // Add client to group
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));

        // Test adding same client again
        assertFalse(parentGroup.add(client));
    }

    @Test
    public void testRemove() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();

        // Add client to group
        parentGroup.add(client);

        // Test remove
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));

        // Test removing same client again
        assertFalse(parentGroup.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);

        // Test createSubGroup
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotNull(subGroup);
        assertEquals(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testAddClientHandler() {
        // Initialize test objects
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();

        // Test addClientHandler
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}

class Client {
}

class ClientHandler {
    Object lastMessageSent;
    Object lastMessageReceived;
    Throwable lastExceptionCaught;

    void messageSent(Client client, Object message) {
        lastMessageSent = message;
    }

    void messageReceived(Client client, Object message) {
        lastMessageReceived = message;
    }

    void exceptionCaught(Client client, Throwable cause) {
        lastExceptionCaught = cause;
    }

    void clientAdded(Client client) {
    }

    void clientRemoved(Client client) {
    }
}