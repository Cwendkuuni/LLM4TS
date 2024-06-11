import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {
    private ClientGroup parentGroup;
    private ClientGroup clientGroup;
    private Client client1;
    private Client client2;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        clientGroup = new ClientGroup(parentGroup);
        client1 = new Client();
        client2 = new Client();

        parentGroup.add(client1);
        clientGroup.add(client2);
    }

    @Test
    public void testParentGroup() {
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testContains() {
        assertTrue(parentGroup.contains(client1));
        assertTrue(clientGroup.contains(client2));
    }

    @Test
    public void testSize() {
        assertEquals(1, parentGroup.size());
        assertEquals(1, clientGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertFalse(parentGroup.add(client2));
        assertTrue(clientGroup.add(client2));
        assertFalse(clientGroup.add(client1));
    }

    @Test
    public void testRemove() {
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.remove(client2));
        assertTrue(clientGroup.remove(client2));
        assertFalse(clientGroup.remove(client1));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.iterator().hasNext());
        assertTrue(subGroup.iterator().hasNext());
        assertTrue(subGroup.contains(client1));
    }

    @Test
    public void testAddClientHandler() {
        ClientHandler handler = new ClientHandler();
        parentGroup.addClientHandler(handler);
        assertEquals(handler, parentGroup.handler);
    }
}