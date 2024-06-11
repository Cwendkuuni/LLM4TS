import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InternalChatFrameTest {
  
  public InternalChatFrameTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
  
  @Test
  public void testAddText() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addText("Test Line 1");
    chatFrame.addText("Test Line 2");
    assertEquals(chatFrame.getChatlog(), "Test Line 1\nTest Line 2\n");
  }
  
  @Test
  public void testAddUser() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addUser("User1");
    chatFrame.addUser("User2");
    assertEquals(chatFrame.getUserlist().length, 2);
    assertEquals(chatFrame.getUserlist()[0], "User2");
    assertEquals(chatFrame.getUserlist()[1], "User1");
  }
  
  @Test
  public void testDeleteUser() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addUser("User1");
    chatFrame.addUser("User2");
    chatFrame.deleteUser("User2");
    assertEquals(chatFrame.getUserlist().length, 1);
    assertEquals(chatFrame.getUserlist()[0], "User1");
  }
  
  @Test
  public void testActionPerformed() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    final ActionEvent e1 = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
    chatFrame.actionPerformed(e1);
    final ActionEvent e2 = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
    chatFrame.actionPerformed(e2);
    final ActionEvent e3 = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
    chatFrame.actionPerformed(e3);
    assertEquals(chatFrame.getTxtInput().getText(), "/w null ");
  }
  
  @Test
  public void testMouseClicked() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    final MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
    chatFrame.mouseClicked(e);
  }
  
  @Test
  public void testMouseEntered() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    final MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 1, false);
    chatFrame.mouseEntered(e);
  }
  
  @Test
  public void testMouseExited() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    final MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, 0, 0, 1, false);
    chatFrame.mouseExited(e);
  }
  
  @Test
  public void testMousePressed() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    final MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
    chatFrame.mousePressed(e);
  }
  
  @Test
  public void testMouseReleased() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    final MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 0, 0, 1, false);
    chatFrame.mouseReleased(e);
  }
  
  @Test
  public void testGetChatlog() {
    final MainFrame parent = new MainFrame();
    final InternalChatFrame chatFrame = new InternalChatFrame(parent);
    chatFrame.addText("Test Line 1");
    chatFrame.addText("Test Line 2");
    assertEquals(chatFrame.getChatlog(), "Test Line 1\nTest Line 2\n");
  }
}