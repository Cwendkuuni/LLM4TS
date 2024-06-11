import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

  @Test
  public void testActionPerformed_ExitCommand() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
    gui.actionPerformed(e);
    // check if the system exits with code -1
  }

  @Test
  public void testActionPerformed_KnownAuthorCommand() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
    gui.actionPerformed(e);
    // check if currentAuthor text field is enabled and empty
  }

  @Test
  public void testActionPerformed_UnknownAuthorCommand() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
    gui.actionPerformed(e);
    // check if currentAuthor text field is disabled and empty
  }

  // similar tests can be written for other action commands

  @Test
  public void testLoadDemo() {
    jgaapGUI gui = new jgaapGUI();
    gui.loadDemo();
    // check if the loadDemo method is called correctly
  }
}