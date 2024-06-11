package ioproject.server.network;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        // Test when parent group is null
        ClientGroup parentGroup = null;
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        assertNull(clientGroup.parentGroup());
        
        // Test when parent group is not null
        parentGroup = new ClientGroup(null);
        clientGroup = new ClientGroup(parentGroup);
        assertEquals(parentGroup, clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();

        // Test when clients set does not contain the client
        boolean result = clientGroup.notifyMessageSent(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups is empty
        clientGroup.add(client);
        result = clientGroup.notifyMessageSent(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup
        ClientGroup subGroup = clientGroup.createSubGroup();
        result = clientGroup.notifyMessageSent(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup 
        // with the client but no handler
        subGroup.add(client);
        result = clientGroup.notifyMessageSent(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup 
        // with the client and handler
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        result = clientGroup.notifyMessageSent(client, message);
        assertTrue(result);
    }
    
    @Test
    public void testNotifyMessageReceived() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();

        // Test when clients set does not contain the client
        boolean result = clientGroup.notifyMessageReceived(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups is empty
        clientGroup.add(client);
        result = clientGroup.notifyMessageReceived(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup
        ClientGroup subGroup = clientGroup.createSubGroup();
        result = clientGroup.notifyMessageReceived(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup 
        // with the client but no handler
        subGroup.add(client);
        result = clientGroup.notifyMessageReceived(client, message);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup 
        // with the client and handler
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        result = clientGroup.notifyMessageReceived(client, message);
        assertTrue(result);
    }
    
    @Test
    public void testNotifyExceptionCaught() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();
        Throwable cause = new Throwable();

        // Test when clients set does not contain the client
        boolean result = clientGroup.notifyExceptionCaught(client, cause);
        assertFalse(result);

        // Test when clients set contains the client and subgroups is empty
        clientGroup.add(client);
        result = clientGroup.notifyExceptionCaught(client, cause);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup
        ClientGroup subGroup = clientGroup.createSubGroup();
        result = clientGroup.notifyExceptionCaught(client, cause);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup 
        // with the client but no handler
        subGroup.add(client);
        result = clientGroup.notifyExceptionCaught(client, cause);
        assertFalse(result);

        // Test when clients set contains the client and subgroups contains one subgroup 
        // with the client and handler
        ClientHandler handler = new ClientHandler();
        clientGroup.addClientHandler(handler);
        result = clientGroup.notifyExceptionCaught(client, cause);
        assertTrue(result);
    }
    
    @Test
    public void testContains() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();

        // Test when clients set is empty
        boolean result = clientGroup.contains(client);
        assertFalse(result);

        // Test when clients set contains the client
        clientGroup.add(client);
        result = clientGroup.contains(client);
        assertTrue(result);
    }
    
    @Test
    public void testSize() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();

        // Test when clients set is empty
        int size = clientGroup.size();
        assertEquals(0, size);

        // Test when clients set contains 3 clients
        clientGroup.add(client1);
        clientGroup.add(client2);
        clientGroup.add(client3);
        size = clientGroup.size();
        assertEquals(3, size);
    }
    
    @Test
    public void testAdd() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();

        // Test when parent group is null
        boolean result = clientGroup.add(client);
        assertTrue(result);
        assertTrue(clientGroup.contains(client));

        // Test when parent group contains the client
        parentGroup.add(client);
        result = clientGroup.add(client);
        assertTrue(result);
        assertTrue(clientGroup.contains(client));

        // Test when parent group does not contain the client
        Client client2 = new Client();
        result = clientGroup.add(client2);
        assertFalse(result);
        assertFalse(clientGroup.contains(client2));
    }
    
    @Test
    public void testRemove() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        Client client = new Client();

        // Test when clients set does not contain the client
        boolean result = clientGroup.remove(client);
        assertFalse(result);

        // Test when clients set contains the client and no subgroups
        clientGroup.add(client);
        result = clientGroup.remove(client);
        assertTrue(result);
        assertFalse(clientGroup.contains(client));

        // Test when clients set contains the client and one subgroup with the client
        ClientGroup subGroup = clientGroup.createSubGroup();
        subGroup.add(client);
        result = clientGroup.remove(client);
        assertTrue(result);
        assertFalse(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));

        // Test when clients set contains the client and one subgroup without the client
        Client client2 = new Client();
        subGroup.add(client2);
        result = clientGroup.remove(client);
        assertTrue(result);
        assertFalse(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));

        // Test when clients set contains the client and multiple subgroups with and without the client
        ClientGroup subGroup2 = clientGroup.createSubGroup();
        subGroup2.add(client);
        subGroup.add(client2);
        result = clientGroup.remove(client);
        assertTrue(result);
        assertFalse(clientGroup.contains(client));
        assertFalse(subGroup.contains(client));
        assertTrue(subGroup2.contains(client));
        assertFalse(subGroup.contains(client2));
    }

    @Test
    public void testCreateSubGroup() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);

        // Test when no subgroups exist
        ClientGroup subGroup = clientGroup.createSubGroup();
        assertNotNull(subGroup);
        assertFalse(clientGroup.subGroups.isEmpty());
        
        // Test when subgroups exist
        ClientGroup subGroup2 = clientGroup.createSubGroup();
        assertNotNull(subGroup2);
        assertEquals(2, clientGroup.subGroups.size());
    }
    
    @Test
    public void testAddClientHandler() {
        // Initialize test data
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        ClientHandler handler = new ClientHandler();

        // Test when handler is added to client group
        clientGroup.addClientHandler(handler);
        assertEquals(handler, clientGroup.handler);
    }
}