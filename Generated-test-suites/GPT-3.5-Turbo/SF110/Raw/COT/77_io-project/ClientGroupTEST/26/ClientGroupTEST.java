
import org.junit.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

   @Test
   public void testParentGroup() {
      ClientGroup parentGroup = new ClientGroup(null);
      ClientGroup clientGroup = new ClientGroup(parentGroup);
      assertEquals(parentGroup, clientGroup.parentGroup());
   }

   @Test
   public void testNotifyMessageSent() {
      Client client = new Client();
      Object message = new Object();
      ClientGroup clientGroup = new ClientGroup(null);
      assertFalse(clientGroup.notifyMessageSent(client, message));
   }

   @Test
   public void testNotifyMessageReceived() {
      Client client = new Client();
      Object message = new Object();
      ClientGroup clientGroup = new ClientGroup(null);
      assertFalse(clientGroup.notifyMessageReceived(client, message));
   }

   @Test
   public void testNotifyExceptionCaught() {
      Client client = new Client();
      Throwable cause = new Throwable();
      ClientGroup clientGroup = new ClientGroup(null);
      assertFalse(clientGroup.notifyExceptionCaught(client, cause));
   }

   @Test
   public void testContains() {
      Client client = new Client();
      ClientGroup clientGroup = new ClientGroup(null);
      assertFalse(clientGroup.contains(client));
   }

   @Test
   public void testSize() {
      ClientGroup clientGroup = new ClientGroup(null);
      assertEquals(0, clientGroup.size());
   }

   @Test
   public void testAdd() {
      Client client = new Client();
      ClientGroup clientGroup = new ClientGroup(null);
      assertTrue(clientGroup.add(client));
   }

   @Test
   public void testRemove() {
      Client client = new Client();
      ClientGroup clientGroup = new ClientGroup(null);
      assertFalse(clientGroup.remove(client));
   }

   @Test
   public void testCreateSubGroup() {
      ClientGroup parentGroup = new ClientGroup(null);
      ClientGroup clientGroup = new ClientGroup(parentGroup);
      assertEquals(parentGroup, clientGroup.createSubGroup().parentGroup());
   }

   @Test
   public void testAddClientHandler() {
      ClientHandler handler = new ClientHandler();
      ClientGroup clientGroup = new ClientGroup(null);
      clientGroup.addClientHandler(handler);
      assertEquals(handler, clientGroup.createSubGroup().handler);
   }

}
