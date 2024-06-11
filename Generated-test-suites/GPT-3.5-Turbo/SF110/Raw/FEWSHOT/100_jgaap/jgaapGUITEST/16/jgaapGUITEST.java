
//import statements

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {
  
  private jgaapGUI gui;
  
  @Before
  public void setUp() {
    gui = new jgaapGUI();
  }

  @Test
  public void testActionPerformed() {
    // Test the Exit action command
    assertEquals(System.exit(-1), gui.actionPerformed(new ActionEvent(new Object(), 0, "Exit")));

    // Test the knownauthor action command
    gui.currentAuthor.setText("John Doe");
    gui.currentAuthor.setEnabled(true);
    assertEquals("John Doe", gui.currentAuthor.getText());
    assertEquals(true, gui.currentAuthor.isEnabled());
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "knownauthor")));

    // Test the unknownauthor action command
    gui.currentAuthor.setText("Jane Doe");
    gui.currentAuthor.setEnabled(false);
    assertEquals("Jane Doe", gui.currentAuthor.getText());
    assertEquals(false, gui.currentAuthor.isEnabled());
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "unknownauthor")));

    // Test the Import action command
    gui.buttonGroup1.setSelected(gui.radioKnown.getModel(), true);
    gui.currentAuthor.setText("John Doe");
    assertEquals("John Doe", gui.currentAuthor.getText());
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "Import")));
    gui.buttonGroup1.setSelected(gui.radioUnknown.getModel(), true);
    gui.currentAuthor.setText("Jane Doe");
    assertEquals("Jane Doe", gui.currentAuthor.getText());
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "Import")));

    // Test the Canonicize action command
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "Canonicize")));

    // Test the CreateEventSet action command
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "CreateEventSet")));

    // Test the Analyze action command
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "Analyze")));

    // Test the demo action command
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "demo")));

    // Test unknown action commands
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "unknown")));
    assertEquals(null, gui.actionPerformed(new ActionEvent(new Object(), 0, "example")));
  }
  
  @Test
  public void testLoadDemo() {
    assertEquals(null, gui.loadDemo());
  }
  
}
