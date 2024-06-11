import org.junit.*;
import static org.junit.Assert.*;
import java.lang.ref.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        assertEquals(parentGroup, clientGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_withValidClientAndMessage() {
        Client client = new Client();
        Object message = new Object();
        ClientGroup clientGroup = new ClientGroup(null);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }
    
    @Test
    public void testNotifyMessageSent_withInvalidClient() {
        Client client = new Client();
        Object message = new Object();
        ClientGroup clientGroup = new ClientGroup(null);
        assertFalse(clientGroup.notifyMessageSent(client, message));
    }
    
    // Write similar test methods for the remaining methods in the ClientGroup class

}