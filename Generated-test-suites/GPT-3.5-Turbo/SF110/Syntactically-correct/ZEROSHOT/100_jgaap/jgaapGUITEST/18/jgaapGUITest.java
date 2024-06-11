import org.junit.*;
import static org.junit.Assert.*;

public class jgaapGUITest {

   @Test
   public void testActionPerformed_Exit() {
      jgaapGUI gui = new jgaapGUI();
      ActionEvent e = new ActionEvent(new Object(), 1, "Exit");
      gui.actionPerformed(e);
      // Assert exit code
   }

   @Test
   public void testActionPerformed_KnownAuthor() {
      jgaapGUI gui = new jgaapGUI();
      ActionEvent e = new ActionEvent(new Object(), 1, "knownauthor");
      gui.actionPerformed(e);
      // Assert currentAuthor text field is enabled
   }

   @Test
   public void testActionPerformed_UnknownAuthor() {
      jgaapGUI gui = new jgaapGUI();
      ActionEvent e = new ActionEvent(new Object(), 1, "unknownauthor");
      gui.actionPerformed(e);
      // Assert currentAuthor text field is disabled
   }

   @Test
   public void testActionPerformed_Import() {
      // Need to mock JFileChooser and guiDriver to perform this test
   }

   @Test
   public void testActionPerformed_Canonicize() {
      // Need to mock preprocessEngineDriver and guiDriver to perform this test
   }

   @Test
   public void testActionPerformed_CreateEventSet() {
      // Need to mock createEventSetDriver and guiDriver to perform this test
   }

   @Test
   public void testActionPerformed_Analyze() {
      // Need to mock runStatisticalAnalysisDriver and guiDriver to perform this test
   }

   @Test
   public void testActionPerformed_Demo() {
      // Need to mock loadDemo and guiDriver to perform this test
   }

}