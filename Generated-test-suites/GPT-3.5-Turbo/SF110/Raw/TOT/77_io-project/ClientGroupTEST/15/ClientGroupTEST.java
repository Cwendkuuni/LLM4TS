import org.junit.*;
import static org.junit.Assert.*;
import java.util.Iterator;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup childGroup;
    private ClientGroup grandchildGroup;
    private ClientHandler handler;
    private Client client1;
    private Client client2;
    private Client client3;


    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        childGroup = parentGroup.createSubGroup();
        grandchildGroup = childGroup.createSubGroup();
        handler = new ClientHandler();
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
    }

    @Test
    public void testParentGroup() {
        assertEquals(parentGroup, childGroup.parentGroup());
        assertEquals(childGroup, grandchildGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        assertFalse(parentGroup.notifyMessageSent(client1, "Hello"));
        childGroup.add(client1);
        assertTrue(childGroup.notifyMessageSent(client1, "Hello"));
        assertFalse(grandchildGroup.notifyMessageSent(client1, "Hello"));
        childGroup.add(client2);
        childGroup.addClientHandler(handler);
        assertTrue(childGroup.notifyMessageSent(client2, "Hello"));
        assertFalse(childGroup.notifyMessageSent(client3, "Hello"));
        assertEquals("Hello", handler.getMessageSent());
    }

    @Test
    public void testNotifyMessageReceived() {
        assertFalse(parentGroup.notifyMessageReceived(client1, "Hello"));
        childGroup.add(client1);
        assertTrue(childGroup.notifyMessageReceived(client1, "Hello"));
        assertFalse(grandchildGroup.notifyMessageReceived(client1, "Hello"));
        childGroup.add(client2);
        childGroup.addClientHandler(handler);
        assertTrue(childGroup.notifyMessageReceived(client2, "Hello"));
        assertFalse(childGroup.notifyMessageReceived(client3, "Hello"));
        assertEquals("Hello", handler.getMessageReceived());
    }

    @Test
    public void testNotifyExceptionCaught() {
        assertFalse(parentGroup.notifyExceptionCaught(client1, new Exception()));
        childGroup.add(client1);
        assertTrue(childGroup.notifyExceptionCaught(client1, new Exception()));
        assertFalse(grandchildGroup.notifyExceptionCaught(client1, new Exception()));
        childGroup.add(client2);
        childGroup.addClientHandler(handler);
        assertTrue(childGroup.notifyExceptionCaught(client2, new Exception()));
        assertFalse(childGroup.notifyExceptionCaught(client3, new Exception()));
        assertEquals("Exception", handler.getExceptionCaught().getClass().getSimpleName());
    }

    @Test
    public void testIterator() {
        assertFalse(parentGroup.iterator().hasNext());
        childGroup.add(client1);
        Iterator<Client> iterator = childGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertFalse(iterator.hasNext());
        childGroup.add(client2);
        iterator = childGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        assertFalse(parentGroup.contains(client1));
        childGroup.add(client1);
        assertTrue(childGroup.contains(client1));
        assertFalse(childGroup.contains(client2));
        grandchildGroup.add(client2);
        assertTrue(grandchildGroup.contains(client2));
        assertFalse(grandchildGroup.contains(client1));
    }

    @Test
    public void testSize() {
        assertEquals(0, parentGroup.size());
        childGroup.add(client1);
        assertEquals(1, childGroup.size());
        childGroup.add(client2);
        assertEquals(2, childGroup.size());
        grandchildGroup.add(client3);
        assertEquals(3, grandchildGroup.size());
        childGroup.remove(client2);
        assertEquals(2, childGroup.size());
        grandchildGroup.remove(client3);
        assertEquals(1, grandchildGroup.size());
    }

    @Test
    public void testAdd() {
        assertTrue(parentGroup.add(client1));
        assertTrue(parentGroup.contains(client1));
        assertTrue(childGroup.add(client2));
        assertTrue(childGroup.contains(client2));
        assertFalse(childGroup.add(client1));
        assertFalse(grandchildGroup.add(client1));
        assertFalse(grandchildGroup.contains(client1));
    }

    @Test
    public void testRemove() {
        assertFalse(parentGroup.remove(client1));
        childGroup.add(client1);
        assertTrue(childGroup.remove(client1));
        assertFalse(childGroup.contains(client1));
        childGroup.add(client2);
        grandchildGroup.add(client3);
        assertTrue(grandchildGroup.remove(client3));
        assertFalse(grandchildGroup.contains(client3));
        assertFalse(childGroup.remove(client3));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotSame(parentGroup, subGroup);
        assertTrue(subGroup.parentGroup().equals(parentGroup));
        assertTrue(subGroup.parentGroup().subGroups.contains(subGroup));
    }

    @Test
    public void testAddClientHandler() {
        assertNull(childGroup.handler);
        childGroup.addClientHandler(handler);
        assertNotNull(childGroup.handler);
        assertSame(handler, childGroup.handler);
    }

    private static class Client {
    }
    
    private static class ClientHandler {
        private Object messageSent;
        private Object messageReceived;
        private Throwable exceptionCaught;
        
        public void messageSent(Client client, Object message) {
            messageSent = message;
        }
        
        public void messageReceived(Client client, Object message) {
            messageReceived = message;
        }
        
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaught = cause;
        }
        
        public Object getMessageSent() {
            return messageSent;
        }
        
        public Object getMessageReceived() {
            return messageReceived;
        }
        
        public Throwable getExceptionCaught() {
            return exceptionCaught;
        }
    }

}