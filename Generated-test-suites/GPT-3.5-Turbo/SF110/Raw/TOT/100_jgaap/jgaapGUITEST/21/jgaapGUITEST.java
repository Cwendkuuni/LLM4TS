import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

  // Testing the constructor
  @Test
  public void testConstructor() {
    jgaapGUI gui = new jgaapGUI();
    assertNotNull(gui);
  }

  // Testing the actionPerformed method
  // Test case for "Exit" command
  @Test
  public void testActionPerformedExit() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
    gui.actionPerformed(e);
    // Check if the application exits
  }

  // Test case for "knownauthor" command
  @Test
  public void testActionPerformedKnownAuthor() {
    jgaapGUI gui = new jgaapGUI();
    gui.currentAuthor.setText("John Doe");
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
    gui.actionPerformed(e);
    assertEquals("John Doe", gui.currentAuthor.getText());
    assertTrue(gui.currentAuthor.isEnabled());
  }

  // Test case for "unknownauthor" command
  @Test
  public void testActionPerformedUnknownAuthor() {
    jgaapGUI gui = new jgaapGUI();
    gui.currentAuthor.setText("John Doe");
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
    gui.actionPerformed(e);
    assertEquals("", gui.currentAuthor.getText());
    assertFalse(gui.currentAuthor.isEnabled());
  }

  // Testing the loadDemo method
  @Test
  public void testLoadDemo() {
    jgaapGUI gui = new jgaapGUI();
    gui.loadDemo();
    // Check if the demo is loaded successfully
  }

  // Add more test cases for each method...

}