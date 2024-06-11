
package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class ClientGroupTest {

    private Client client;
    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientHandler handler;

    @Before
    public void setUp() {
        client = new Client();
        parentGroup = new ClientGroup(null);
        subGroup = parentGroup.createSubGroup();
        handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
    }

    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_Success() {
        assertTrue(parentGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageSent_Failure() {
        assertFalse(parentGroup.notifyMessageSent(new Client(), "message"));
    }

    @Test
    public void testNotifyMessageReceived_Success() {
        assertTrue(parentGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived_Failure() {
        assertFalse(parentGroup.notifyMessageReceived(new Client(), "message"));
    }

    @Test
    public void testNotifyExceptionCaught_Success() {
        assertTrue(parentGroup.notifyExceptionCaught(client, new Throwable()));
    }

    @Test
    public void testNotifyExceptionCaught_Failure() {
        assertFalse(parentGroup.notifyExceptionCaught(new Client(), new Throwable()));
    }

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
        parentGroup.add(client);
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAdd_Success() {
        assertTrue(parentGroup.add(client));
    }

    @Test
    public void testAdd_Failure() {
        assertFalse(parentGroup.add(new Client()));
    }

    @Test
    public void testRemove_Success() {
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
    }

    @Test
    public void testRemove_Failure() {
        assertFalse(parentGroup.remove(new Client()));
    }

    @Test
    public void testCreateSubGroup() {
        assertNotNull(parentGroup.createSubGroup());
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(new ClientHandler());
        assertEquals(handler, parentGroup.handler);
    }
}
