import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

  
  @Test
  public void testAddText() {
    // Test typical use case
    InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addText("Test message");
    assertEquals("Test message\n", chatFrame.getChatlog());
    
    // Test empty line
    chatFrame.addText("");
    assertEquals("Test message\n\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddUser() {
    // Test typical use case
    InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addUser("User1");
    String[] userList = chatFrame.getUserlist();
    assertEquals("User1", userList[0]);
    
    // Test adding multiple users
    chatFrame.addUser("User2");
    chatFrame.addUser("User3");
    userList = chatFrame.getUserlist();
    assertEquals(3, userList.length);
    assertEquals("User3", userList[0]);
    assertEquals("User2", userList[1]);
    assertEquals("User1", userList[2]);
  }
  
  @Test
  public void testDeleteUser() {
    // Test typical use case
    InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addUser("User1");
    chatFrame.addUser("User2");
    chatFrame.deleteUser("User1");
    String[] userList = chatFrame.getUserlist();
    assertEquals(1, userList.length);
    assertEquals("User2", userList[0]);
  }
  
  @Test
  public void testActionPerformed() {
    // Test choosing color for user
    InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addUser("User1");
    chatFrame.list.setSelectedIndex(0);
    chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
    // Check if the color for selected user was changed
    
    // Test choosing background color
    chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
    // Check if the background color of the text pane was changed
    
    // Test whisper action
    chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
    assertEquals("/w User1 ", chatFrame.txtInput.getText());
  }

}