package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client = Mockito.mock(Client.class);
        handler = Mockito.mock(ClientHandler.class);
    }

    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_NotInGroup() {
        assertFalse(parentGroup.notifyMessageSent(client, "Message"));
    }

    @Test
    public void testNotifyMessageSent_InSubGroup() {
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "Message"));
    }

    @Test
    public void testNotifyMessageSent_HandlerNotNull() {
        parentGroup.add(client);
        parentGroup.addClientHandler(handler);

        parentGroup.notifyMessageSent(client, "Message");

        Mockito.verify(handler).messageSent(client, "Message");
    }

    // Similar tests for other methods...

    @Test
    public void testContains_True() {
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testContains_False() {
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());

        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAdd_ParentContainsClient() {
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testAdd_ParentDoesNotContainClient() {
        assertFalse(parentGroup.add(client));
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testRemove_ClientIsMember() {
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testRemove_ClientIsNotMember() {
        assertFalse(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotNull(subGroup);
        assertTrue(parentGroup.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}