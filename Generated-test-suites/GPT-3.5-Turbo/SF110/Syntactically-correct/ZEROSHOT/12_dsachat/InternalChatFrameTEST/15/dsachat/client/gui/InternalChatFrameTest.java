import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
  
  @Test
  public void testAddText() {
    // Test addText method with different inputs
    InternalChatFrame frame = new InternalChatFrame();
    
    // Test adding text without ":" in it
    frame.addText("Hello, world");
    assertEquals("Hello, world\n", frame.getChatlog());
    
    // Test adding text with ":" in it
    frame.addText("User: Hello");
    assertEquals("Hello, world\nUser: Hello\n", frame.getChatlog());
    
    // Test adding text with "*"
    frame.addText("User1 * This is bold * This is not * also bold");
    assertEquals("Hello, world\nUser: Hello\nUser1 This is bold This is not also bold\n", frame.getChatlog());
    
    // Test adding text with "_"
    frame.addText("User2 + This is italic + This is not + also italic");
    assertEquals("Hello, world\nUser: Hello\nUser1 This is bold This is not also bold\nUser2 This is italic This is not also italic\n", frame.getChatlog());
  }
  
  @Test
  public void testAddUser() {
    // Test addUser method with different inputs
    InternalChatFrame frame = new InternalChatFrame();
    
    // Test adding single user
    frame.addUser("User1");
    assertEquals("User1\n", frame.getUserlist()[0]);
    
    // Test adding multiple users
    frame.addUser("User2");
    frame.addUser("User3");
    assertEquals("User2", frame.getUserlist()[0]);
    assertEquals("User1", frame.getUserlist()[2]);
  }
  
  @Test
  public void testDeleteUser() {
    // Test deleteUser method with different inputs
    InternalChatFrame frame = new InternalChatFrame();
    
    // Test deleting single user
    frame.addUser("User1");
    frame.addUser("User2");
    frame.deleteUser("User1");
    assertEquals("User2", frame.getUserlist()[0]);
    
    // Test deleting non-existent user
    frame.deleteUser("User3");
    assertEquals(1, frame.getUserlist().length);
  }
  
  @Test
  public void testActionPerformed() {
    // Test actionPerformed method with different inputs
    InternalChatFrame frame = new InternalChatFrame();
    
    // Test with action command "chooseColor"
    frame.addUser("User1");
    frame.list.setSelectedIndex(0);
    frame.actionPerformed(new ActionEvent(frame, 0, "chooseColor"));
    // assert code to check the color change of user
    
    // Test with action command "bgColor"
    frame.actionPerformed(new ActionEvent(frame, 0, "bgColor"));
    // assert code to check the background color change
    
    // Test with action command "whisper"
    frame.actionPerformed(new ActionEvent(frame, 0, "whisper"));
    assertEquals("/w User1 ", frame.txtInput.getText());
  }
  
  @Test
  public void testMousePressed() {
    // Test mousePressed method with different inputs
    InternalChatFrame frame = new InternalChatFrame();
    
    // Test with list as the source of the event
    MouseEvent event1 = new MouseEvent(frame.list, 0, 0, 0, 0, 0, 0, false);
    frame.mousePressed(event1);
    // assert code to check if listPop is shown
    
    // Test with showTxt as the source of the event
    MouseEvent event2 = new MouseEvent(frame.showTxt, 0, 0, 0, 0, 0, 0, false);
    frame.mousePressed(event2);
    // assert code to check if txtPop is shown
  }
  
  @Test
  public void testMouseReleased() {
    // Test mouseReleased method with different inputs
    InternalChatFrame frame = new InternalChatFrame();
    
    // Test with list as the source of the event
    MouseEvent event1 = new MouseEvent(frame.list, 0, 0, 0, 0, 0, 0, false);
    frame.mouseReleased(event1);
    // assert code to check if listPop is shown
    
    // Test with showTxt as the source of the event
    MouseEvent event2 = new MouseEvent(frame.showTxt, 0, 0, 0, 0, 0, 0, false);
    frame.mouseReleased(event2);
    // assert code to check if txtPop is shown
  }
  
  @Test
  public void testGetChatlog() {
    // Test getChatlog method
    InternalChatFrame frame = new InternalChatFrame();
    
    frame.addText("Hello, world");
    assertEquals("Hello, world\n", frame.getChatlog());
    
    frame.addText("User1: Hello");
    assertEquals("Hello, world\nUser1: Hello\n", frame.getChatlog());
  }
}