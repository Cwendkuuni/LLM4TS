import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

  @Test
  public void testActionListener_Exit() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
    gui.actionPerformed(e);
    // Assertion to check if the application has exited
  }

  @Test
  public void testActionListener_KnownAuthor() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "knownauthor");
    gui.actionPerformed(e);
    // Assertion to check if the "currentAuthor" text field is enabled
  }

  @Test
  public void testActionListener_UnknownAuthor() {
    jgaapGUI gui = new jgaapGUI();
    ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "unknownauthor");
    gui.actionPerformed(e);
    // Assertion to check if the "currentAuthor" text field is disabled
  }

  // Add more test cases for each action listener event

  @Test
  public void testLoadDemo() {
    jgaapGUI gui = new jgaapGUI();
    gui.loadDemo();
    // Assertion to check if the loadDemo method has executed successfully
  }

}