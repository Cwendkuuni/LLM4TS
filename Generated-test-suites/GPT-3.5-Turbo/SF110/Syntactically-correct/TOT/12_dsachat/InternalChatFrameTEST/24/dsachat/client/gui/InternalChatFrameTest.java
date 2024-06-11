import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {
  
  private InternalChatFrame chatFrame;
  
  @Before
  public void setup() {
    chatFrame = new InternalChatFrame();
  }
  
  @Test
  public void testAddText_systemMessage() {
    chatFrame.addText("System message");
    String chatlog = chatFrame.getChatlog();
    assertEquals("System message\n", chatlog);
  }
  
  @Test
  public void testAddText_regularMessage() {
    chatFrame.addText("Username: Hello, World!");
    String chatlog = chatFrame.getChatlog();
    assertEquals("Username: Hello, World!\n", chatlog);
  }
  
  @Test
  public void testAddText_italicAndBoldMessage() {
    chatFrame.addText("Username: *Italic* +Bold+ message");
    String chatlog = chatFrame.getChatlog();
    assertEquals("Username: Italic Bold message\n", chatlog);
  }
  
  @Test
  public void testAddText_italicMessage() {
    chatFrame.addText("Username: *Italic* message");
    String chatlog = chatFrame.getChatlog();
    assertEquals("Username: Italic message\n", chatlog);
  }
  
  @Test
  public void testAddText_boldMessage() {
    chatFrame.addText("Username: +Bold+ message");
    String chatlog = chatFrame.getChatlog();
    assertEquals("Username: Bold message\n", chatlog);
  }
  
  @Test
  public void testAddUser() {
    chatFrame.addUser("Alice");
    String[] userlist = chatFrame.getUserlist();
    assertEquals(1, userlist.length);
    assertEquals("Alice", userlist[0]);
  }
  
  @Test
  public void testDeleteUser() {
    chatFrame.addUser("Bob");
    chatFrame.deleteUser("Bob");
    String[] userlist = chatFrame.getUserlist();
    assertEquals(0, userlist.length);
  }
  
  @Test
  public void testGetUserlist() {
    chatFrame.addUser("Charlie");
    chatFrame.addUser("David");
    String[] userlist = chatFrame.getUserlist();
    assertEquals(2, userlist.length);
    assertEquals("Charlie", userlist[0]);
    assertEquals("David", userlist[1]);
  }
  
}