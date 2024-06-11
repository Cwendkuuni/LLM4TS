import static org.junit.Assert.*;
import org.junit.Test;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        assertEquals(parentGroup, group.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // test normal functionality
        // test case where client is not in any sub-group
        // test case where no handler is set
        // test edge case where client is in a sub-group
        // test exception handling scenario
    }

    @Test
    public void testNotifyMessageReceived() {
        // test normal functionality
        // test case where client is not in any sub-group
        // test case where no handler is set
        // test edge case where client is in a sub-group
        // test exception handling scenario
    }

    @Test
    public void testNotifyExceptionCaught() {
        // test normal functionality
        // test case where client is not in any sub-group
        // test case where no handler is set
        // test edge case where client is in a sub-group
        // test exception handling scenario
    }

    @Test
    public void testContains() {
        Client client = new Client();
        ClientGroup group = new ClientGroup(null);
        group.add(client);
        assertTrue(group.contains(client));
    }

    @Test
    public void testSize() {
        Client client1 = new Client();
        Client client2 = new Client();
        ClientGroup group = new ClientGroup(null);
        group.add(client1);
        group.add(client2);
        assertEquals(2, group.size());
    }

    @Test
    public void testAdd() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        assertTrue(group.add(client));
    }

    @Test
    public void testRemove() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        group.add(client);
        assertTrue(group.remove(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        assertNotNull(group.createSubGroup());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }
}