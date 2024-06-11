import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
  
  private InternalChatFrame chatFrame;
  private MainFrame parent;
  
  @Before
  public void setUp() {
    parent = new MainFrame();
    chatFrame = new InternalChatFrame(parent);
  }
  
  @Test
  public void testAddText() {
    chatFrame.addText("This is a test message");
    assertEquals("This is a test message\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddUser() {
    chatFrame.addUser("testUser");
    String[] userList = chatFrame.getUserlist();
    assertEquals(1, userList.length);
    assertEquals("testUser", userList[0]);
  }
  
  @Test
  public void testDeleteUser() {
    chatFrame.addUser("testUser");
    chatFrame.deleteUser("testUser");
    String[] userList = chatFrame.getUserlist();
    assertEquals(0, userList.length);
  }
  
}