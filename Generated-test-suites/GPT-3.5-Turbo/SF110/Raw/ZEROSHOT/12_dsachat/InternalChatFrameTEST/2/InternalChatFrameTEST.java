import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class InternalChatFrameTest {
  
  @Test
  public void testCreateGUI() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      assertNotNull(chatFrame.list);
      assertNotNull(chatFrame.listModel);
      assertNotNull(chatFrame.showTxt);
      assertNotNull(chatFrame.txtInput);
      assertNotNull(chatFrame.doc);
      assertNotNull(chatFrame.listPop);
      assertNotNull(chatFrame.txtPop);
  }
  
  @Test
  public void testAddText() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      String line = "Test line";
      chatFrame.addText(line);
      
      assertEquals(line + "\n", chatFrame.getChatlog());
  }
  
  @Test
  public void testAddUser() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      String user = "TestUser";
      chatFrame.addUser(user);
      
      String[] userList = chatFrame.getUserlist();
      assertEquals(user, userList[0]);
  }
  
  @Test
  public void testDeleteUser() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      String user = "TestUser";
      chatFrame.addUser(user);
      chatFrame.deleteUser(user);
      
      String[] userList = chatFrame.getUserlist();
      assertEquals(0, userList.length);
  }
  
  @Test
  public void testActionPerformed() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
      chatFrame.actionPerformed(e);
      
      e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
      chatFrame.actionPerformed(e);
      
      e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
      chatFrame.actionPerformed(e);
      
      assertNotNull(chatFrame.txtInput.getText());
  }
  
  @Test
  public void testMouseEvent() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      MouseEvent e = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 0, false);
      chatFrame.mouseClicked(e);
      
      e = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0, 0, false);
      chatFrame.mouseEntered(e);
      
      e = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_EXITED, 0, 0, 0, 0, 0, false);
      chatFrame.mouseExited(e);
      
      e = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 0, false);
      chatFrame.mousePressed(e);
      
      e = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 0, false);
      chatFrame.mouseReleased(e);
      
      assertNotNull(chatFrame.txtPop);
      assertNotNull(chatFrame.listPop);
  }

  @Test
  public void testGetChatlog() {
      MainFrame parent = new MainFrame();
      InternalChatFrame chatFrame = new InternalChatFrame(parent);
      
      String line = "Test line";
      chatFrame.addText(line);
      
      assertEquals(line + "\n", chatFrame.getChatlog());
  }
}