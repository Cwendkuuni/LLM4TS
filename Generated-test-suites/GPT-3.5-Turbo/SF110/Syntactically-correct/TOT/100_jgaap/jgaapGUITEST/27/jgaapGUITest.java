import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
      
        // Mock the ActionEvent with the "Exit" command
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "Exit");
      
        // Call the actionPerformed method and check if the system exits
        gui.actionPerformed(e);
        assertTrue(System.exitCalled);
    }
  
    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
      
        // Mock the ActionEvent with the "knownauthor" command
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "knownauthor");
      
        // Call the actionPerformed method and check if the currentAuthor field is enabled
        gui.actionPerformed(e);
        assertTrue(gui.currentAuthor.isEnabled());
    }
  
    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
      
        // Mock the ActionEvent with the "unknownauthor" command
        ActionEvent e = new ActionEvent(gui, ActionEvent.ACTION_FIRST, "unknownauthor");
      
        // Call the actionPerformed method and check if the currentAuthor field is disabled
        gui.actionPerformed(e);
        assertFalse(gui.currentAuthor.isEnabled());
    }
  
    // Additional test cases for all other actionPerformed scenarios
  
    // ...

}