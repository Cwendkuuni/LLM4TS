import org.junit.*;
import static org.junit.Assert.*;
import java.lang.ref.*;
import java.util.*;

public class ClientGroupTest {
    private ClientGroup clientGroup;
    private ClientHandlerMock clientHandlerMock;
    
    @Before
    public void setUp() {
        clientGroup = new ClientGroup(null);
        clientHandlerMock = new ClientHandlerMock();
        clientGroup.addClientHandler(clientHandlerMock);
    }
    
    @Test
    public void testParentGroup() {
        assertNull(clientGroup.parentGroup());
    }
    
    @Test
    public void testNotifyMessageSent_existingClientAndNoSubgroups_messageSentToHandler() {
        Client client = new Client("client1");
        clientGroup.add(client);
        
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageSent(client, message));
        assertTrue(clientHandlerMock.isMessageSentCalled());
        assertEquals(client, clientHandlerMock.getLastMessageSentClient());
        assertEquals(message, clientHandlerMock.getLastMessageSent());
    }
    
    @Test
    public void testNotifyMessageSent_existingClientAndSubgroups_messageSentToSubgroups() {
        Client client = new Client("client1");
        clientGroup.add(client);
        
        ClientGroup subGroup1 = clientGroup.createSubGroup();
        ClientGroup subGroup2 = clientGroup.createSubGroup();
        
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageSent(client, message));
        
        assertTrue(clientHandlerMock.isMessageSentCalled());
        assertEquals(client, clientHandlerMock.getLastMessageSentClient());
        assertEquals(message, clientHandlerMock.getLastMessageSent());
        
        assertTrue(subGroup1.clientHandlerMock.isMessageSentCalled());
        assertEquals(client, subGroup1.clientHandlerMock.getLastMessageSentClient());
        assertEquals(message, subGroup1.clientHandlerMock.getLastMessageSent());
        
        assertTrue(subGroup2.clientHandlerMock.isMessageSentCalled());
        assertEquals(client, subGroup2.clientHandlerMock.getLastMessageSentClient());
        assertEquals(message, subGroup2.clientHandlerMock.getLastMessageSent());
    }
    
    @Test
    public void testNotifyMessageSent_nonExistingClient_messageNotSent() {
        Client client = new Client("client1");
        
        Object message = new Object();
        assertFalse(clientGroup.notifyMessageSent(client, message));
        assertFalse(clientHandlerMock.isMessageSentCalled());
    }
    
    // ... other test cases ...
    
    @After
    public void tearDown() {
        clientGroup = null;
        clientHandlerMock = null;
    }
}

class ClientHandlerMock implements ClientHandler {
    private boolean messageSentCalled;
    private Client lastMessageSentClient;
    private Object lastMessageSent;
    
    @Override
    public void messageSent(Client client, Object message) {
        messageSentCalled = true;
        lastMessageSentClient = client;
        lastMessageSent = message;
    }
    
    public boolean isMessageSentCalled() {
        return messageSentCalled;
    }
    
    public Client getLastMessageSentClient() {
        return lastMessageSentClient;
    }
    
    public Object getLastMessageSent() {
        return lastMessageSent;
    }
}