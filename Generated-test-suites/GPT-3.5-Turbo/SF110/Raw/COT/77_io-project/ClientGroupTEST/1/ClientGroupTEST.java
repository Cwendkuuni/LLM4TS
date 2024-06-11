
import static org.junit.Assert.*;
import org.junit.*;

public class ClientGroupTest {
  
  private ClientGroup clientGroup;
  
  @Before
  public void setUp() {
    clientGroup = new ClientGroup(null);
  }
  
  // Test parentGroup method
  @Test
  public void testParentGroup() {
    assertNull(clientGroup.parentGroup());
  }
  
  // Test notifyMessageSent method
  @Test
  public void testNotifyMessageSent() {
    Client client = new Client();
    Object message = new Object();
    
    assertFalse(clientGroup.notifyMessageSent(client, message));
    
    clientGroup.add(client);
    assertTrue(clientGroup.notifyMessageSent(client, message));
  }
  
  // Test notifyMessageReceived method
  @Test
  public void testNotifyMessageReceived() {
    Client client = new Client();
    Object message = new Object();
    
    assertFalse(clientGroup.notifyMessageReceived(client, message));
    
    clientGroup.add(client);
    assertTrue(clientGroup.notifyMessageReceived(client, message));
  }
  
  // Test notifyExceptionCaught method
  @Test
  public void testNotifyExceptionCaught() {
    Client client = new Client();
    Throwable cause = new Throwable();
    
    assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    
    clientGroup.add(client);
    assertTrue(clientGroup.notifyExceptionCaught(client, cause));
  }
  
  // Test contains method
  @Test
  public void testContains() {
    Client client = new Client();
    
    assertFalse(clientGroup.contains(client));
    
    clientGroup.add(client);
    assertTrue(clientGroup.contains(client));
  }
  
  // Test size method
  @Test
  public void testSize() {
    assertEquals(0, clientGroup.size());
    
    Client client1 = new Client();
    Client client2 = new Client();
    
    clientGroup.add(client1);
    assertEquals(1, clientGroup.size());
    
    clientGroup.add(client2);
    assertEquals(2, clientGroup.size());
  }
  
  // Test add method
  @Test
  public void testAdd() {
    Client client = new Client();
    
    assertTrue(clientGroup.add(client));
    assertTrue(clientGroup.contains(client));
    
    ClientGroup subGroup = clientGroup.createSubGroup();
    assertTrue(subGroup.add(client));
    assertTrue(subGroup.contains(client));
  }
  
  // Test remove method
  @Test
  public void testRemove() {
    Client client = new Client();
    
    assertFalse(clientGroup.remove(client));
    
    clientGroup.add(client);
    assertTrue(clientGroup.remove(client));
    assertFalse(clientGroup.contains(client));
  }
  
  // Test createSubGroup method
  @Test
  public void testCreateSubGroup() {
    ClientGroup subGroup = clientGroup.createSubGroup();
    
    assertNotNull(subGroup);
    assertEquals(clientGroup, subGroup.parentGroup());
  }
  
  // Test addClientHandler method
  @Test
  public void testAddClientHandler() {
    ClientHandler handler = new ClientHandler();
    
    clientGroup.addClientHandler(handler);
    
    assertEquals(handler, clientGroup.handler);
  }
  
}

