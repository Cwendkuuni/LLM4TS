package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client1;
    private Client client2;
    private Object message1;
    private Object message2;
    private Throwable throwable;
    private ClientHandler handler;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
        message1 = new Object();
        message2 = new Object();
        throwable = new Throwable();
        handler = new ClientHandler();
    }

    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
        assertEquals(parentGroup, subGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client1, message1));

        parentGroup.add(client1);
        assertTrue(parentGroup.notifyMessageSent(client1, message1));

        subGroup.add(client1);
        assertTrue(subGroup.notifyMessageSent(client1, message1));

        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageSent(client1, message1));
        assertFalse(parentGroup.notifyMessageSent(client1, message2));

        subGroup.addClientHandler(handler);
        assertTrue(subGroup.notifyMessageSent(client1, message1));
        assertFalse(subGroup.notifyMessageSent(client1, message2));
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client1, message1));

        parentGroup.add(client1);
        assertTrue(parentGroup.notifyMessageReceived(client1, message1));

        subGroup.add(client1);
        assertTrue(subGroup.notifyMessageReceived(client1, message1));

        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyMessageReceived(client1, message1));
        assertFalse(parentGroup.notifyMessageReceived(client1, message2));

        subGroup.addClientHandler(handler);
        assertTrue(subGroup.notifyMessageReceived(client1, message1));
        assertFalse(subGroup.notifyMessageReceived(client1, message2));
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client1, throwable));

        parentGroup.add(client1);
        assertTrue(parentGroup.notifyExceptionCaught(client1, throwable));

        subGroup.add(client1);
        assertTrue(subGroup.notifyExceptionCaught(client1, throwable));

        parentGroup.addClientHandler(handler);
        assertTrue(parentGroup.notifyExceptionCaught(client1, throwable));
        assertFalse(parentGroup.notifyExceptionCaught(client1, new Throwable()));

        subGroup.addClientHandler(handler);
        assertTrue(subGroup.notifyExceptionCaught(client1, throwable));
        assertFalse(subGroup.notifyExceptionCaught(client1, new Throwable()));
    }

    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());

        parentGroup.add(client1);
        assertTrue(parentGroup.iterator().hasNext());
        assertEquals(client1, parentGroup.iterator().next());
    }

    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client1));

        parentGroup.add(client1);
        assertTrue(parentGroup.contains(client1));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());

        parentGroup.add(client1);
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertFalse(parentGroup.add(client2));

        subGroup.add(client1);
        assertTrue(subGroup.add(client2));
    }

    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client1));

        parentGroup.add(client1);
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.remove(client2));

        subGroup.add(client1);
        assertTrue(subGroup.remove(client1));
        assertFalse(subGroup.remove(client2));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup newSubGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.iterator().hasNext());
        assertEquals(newSubGroup, parentGroup.iterator().next());
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.add(client1);
        parentGroup.addClientHandler(handler);

        assertEquals(handler, parentGroup.handler);
    }
}