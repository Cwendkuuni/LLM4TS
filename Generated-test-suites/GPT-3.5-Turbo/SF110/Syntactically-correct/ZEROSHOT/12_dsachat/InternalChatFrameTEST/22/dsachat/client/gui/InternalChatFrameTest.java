import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
  
  private InternalChatFrame chatFrame;
  
  @Before
  public void setUp() {
    MainFrame parent = new MainFrame();
    chatFrame = new InternalChatFrame(parent);
  }
  
  @Test
  public void testAddText() {
    chatFrame.addText("This is a test");
    assertEquals("This is a test\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddUser() {
    chatFrame.addUser("User1");
    chatFrame.addUser("User2");
    assertEquals("User1", chatFrame.getUserlist()[0]);
    assertEquals("User2", chatFrame.getUserlist()[1]);
  }
  
  @Test
  public void testDeleteUser() {
    chatFrame.addUser("User1");
    chatFrame.addUser("User2");
    chatFrame.deleteUser("User1");
    assertEquals("User2", chatFrame.getUserlist()[0]);
  }
  
  @Test
  public void testActionPerformed_ChooseColor() {
    ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
    chatFrame.addUser("User1");
    chatFrame.list.setSelectedIndex(0);
    chatFrame.actionPerformed(e);
    // Test whether the color of "User1" style has changed
  }
  
  @Test
  public void testActionPerformed_BgColor() {
    ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
    chatFrame.actionPerformed(e);
    // Test whether the background color of showTxt has changed
  }
  
  @Test
  public void testActionPerformed_Whisper() {
    ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
    chatFrame.addUser("User1");
    chatFrame.list.setSelectedIndex(0);
    chatFrame.actionPerformed(e);
    assertEquals("/w User1 ", chatFrame.txtInput.getText());
  }
}