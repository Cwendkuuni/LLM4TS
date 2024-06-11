import org.junit.Test;
import org.junit.Before;

public class InternalChatFrameTest {
  
  private InternalChatFrame internalChatFrame;
  
  @Before
  public void setup() {
    MainFrame parent = new MainFrame();
    internalChatFrame = new InternalChatFrame(parent);
  }
  
  @Test
  public void testAddText_RegularText() {
    String line = "This is a regular text message";
    internalChatFrame.addText(line);
    String chatlog = internalChatFrame.getChatlog();
    assertTrue(chatlog.contains(line));
  }
  
  @Test
  public void testAddText_MessageWithStyleBoldAndItalic() {
    String line = "This is a *bold* and +italic+ message";
    internalChatFrame.addText(line);
    String chatlog = internalChatFrame.getChatlog();
    assertTrue(chatlog.contains(line));
  }
  
  @Test
  public void testAddText_MessageWithStyleItalic() {
    String line = "This is an *italic* message";
    internalChatFrame.addText(line);
    String chatlog = internalChatFrame.getChatlog();
    assertTrue(chatlog.contains(line));
  }
  
  @Test
  public void testAddText_MessageWithStyleBold() {
    String line = "This is a +bold+ message";
    internalChatFrame.addText(line);
    String chatlog = internalChatFrame.getChatlog();
    assertTrue(chatlog.contains(line));
  }
  
  @Test
  public void testAddText_MessageWithUserStyle() {
    String line = "This is a test message";
    internalChatFrame.addText("User: " + line);
    String chatlog = internalChatFrame.getChatlog();
    assertTrue(chatlog.contains(line));
  }
  
  @Test
  public void testAddText_MessageWithWhisperStyle() {
    String line = "This is a whisper message";
    internalChatFrame.addText("User whispers: " + line);
    String chatlog = internalChatFrame.getChatlog();
    assertTrue(chatlog.contains(line));
  }
  
  @Test
  public void testAddUser_ValidUser() {
    String user = "User";
    internalChatFrame.addUser(user);
    String[] userList = internalChatFrame.getUserlist();
    assertTrue(Arrays.asList(userList).contains(user));
  }
  
  @Test
  public void testDeleteUser_ValidUser() {
    String user = "User";
    internalChatFrame.addUser(user);
    internalChatFrame.deleteUser(user);
    String[] userList = internalChatFrame.getUserlist();
    assertFalse(Arrays.asList(userList).contains(user));
  }
  
}