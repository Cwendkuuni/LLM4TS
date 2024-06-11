import static org.junit.Assert.*;
import org.junit.*;

public class InternalChatFrameTest {
  
  private InternalChatFrame chatFrame;
  
  @Before
  public void setup() {
    chatFrame = new InternalChatFrame(new MainFrame());
  }
  
  @Test
  public void testAddText_regularStyle() {
    chatFrame.addText("Hello World");
    assertEquals("Hello World\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddText_italicStyle() {
    chatFrame.addText("/w TestUser Hello there");
    assertEquals("/w TestUser Hello there\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddText_boldStyle() {
    chatFrame.addText("Test User: **Bold text**");
    assertEquals("Test User: **Bold text**\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddText_boldItalicStyle() {
    chatFrame.addText("Test User: *_Bold and italic text_*");
    assertEquals("Test User: *_Bold and italic text_*\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddText_changeStyle() {
    chatFrame.addText("Test User: **Bold text**");
    chatFrame.addText("Test User: *_Bold and italic text_*");
    chatFrame.changeStyle(14, Color.RED, true, true, "Test User");
    chatFrame.addText("Test User: Updated style");
    assertEquals("Test User: **Bold text**\nTest User: *_Bold and italic text_*\nTest User: Updated style\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddText_whisperStyle() {
    chatFrame.addText("Test User whispers: Hello there");
    assertEquals("Test User whispers: Hello there\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddText_systemStyle() {
    chatFrame.addText("System message");
    assertEquals("System message\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddUser() {
    chatFrame.addUser("Test User");
    String[] userList = chatFrame.getUserlist();
    assertEquals(1, userList.length);
    assertEquals("Test User", userList[0]);
  }
  
  @Test
  public void testDeleteUser() {
    chatFrame.addUser("Test User");
    chatFrame.deleteUser("Test User");
    String[] userList = chatFrame.getUserlist();
    assertEquals(0, userList.length);
  }
  
  @After
  public void cleanup() {
    chatFrame = null;
  }
}