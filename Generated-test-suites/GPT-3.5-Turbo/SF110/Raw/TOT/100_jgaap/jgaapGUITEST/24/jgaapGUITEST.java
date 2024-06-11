import static org.junit.Assert.*;
import org.junit.*;

public class jgaapGUITest {

  @Test
  public void testConstructor() {
    jgaapGUI gui = new jgaapGUI();

    assertNotNull(gui.driver);
    assertNotNull(gui.currentAuthor);
    assertNotNull(gui.buttonGroup1);
    assertNotNull(gui.buttonGroup2);
    assertNotNull(gui.chooseFile);
    assertNotNull(gui.buttonEvSet);
    assertNotNull(gui.paneEvProgress);
    assertNotNull(gui.labelev);
    assertNotNull(gui.progressEventSet);
    assertNotNull(gui.menuBar);
    assertNotNull(gui.menuFile);
    assertNotNull(gui.menuFileExit);
    assertNotNull(gui.menuFileDemo);
    assertNotNull(gui.menuHelp);
    assertNotNull(gui.menuHelpContents);
    assertNotNull(gui.menuHelpAbout);
    assertNotNull(gui.paneOpts);
    assertNotNull(gui.panelAcquire);
    assertNotNull(gui.panelDocs);
    assertNotNull(gui.labelKnown);
    assertNotNull(gui.scrollPane1);
    assertNotNull(gui.listKnown);
    assertNotNull(gui.labelUnknown);
    assertNotNull(gui.scrollPane2);
    assertNotNull(gui.listUnknown);
    assertNotNull(gui.panelOpts);
    assertNotNull(gui.radioKnown);
    assertNotNull(gui.radioUnknown);
    assertNotNull(gui.buttonLocal);
    assertNotNull(gui.panelCanonicize);
    assertNotNull(gui.panelCOpts);
    for (int i = 0; i < gui.canonicizers.length; i++) {
      assertNotNull(gui.canonicizers[i]);
    }
    for (int i = 0; i < gui.eventifiers.length; i++) {
      assertNotNull(gui.eventifiers[i]);
    }
    assertNotNull(gui.esButtonGroup);
    assertNotNull(gui.panelProgress);
    assertNotNull(gui.buttonCanonicize);
    assertNotNull(gui.label1);
    assertNotNull(gui.progressCanonicize);
    assertNotNull(gui.labelCanon);
    assertNotNull(gui.panelAnalyze);
    assertNotNull(gui.labelStatInf);
    assertNotNull(gui.panelMethods);
    assertNotNull(gui.radioCrossEnt);
    assertNotNull(gui.radioLZW);
    assertNotNull(gui.panelAnalProg);
    assertNotNull(gui.buttonAnalyze);
    assertNotNull(gui.labelAnalProg);
    assertNotNull(gui.progressAnalysis);
    assertNotNull(gui.scrollPane3);
    assertNotNull(gui.listResults);
    assertNotNull(gui.buttonEventSet);
    assertNotNull(gui.panelEventSet);
    assertNotNull(gui.panelEOpts);
    assertNotNull(gui.panelEvProgress);
    assertNotNull(gui.labelevSet);
  }

  @Test
  public void testActionPerformedExit() {
    jgaapGUI gui = new jgaapGUI();
    
    ActionEvent event = new ActionEvent(gui.menuFileExit, ActionEvent.ACTION_PERFORMED, "Exit");
    gui.actionPerformed(event);

    // Assert that the program exits with status -1
  }

  @Test
  public void testActionPerformedKnownAuthor() {
    jgaapGUI gui = new jgaapGUI();
    
    ActionEvent event = new ActionEvent(gui.radioKnown, ActionEvent.ACTION_PERFORMED, "knownauthor");
    gui.actionPerformed(event);

    assertFalse(gui.currentAuthor.isEnabled());
    assertEquals("", gui.currentAuthor.getText());
  }

  @Test
  public void testActionPerformedUnknownAuthor() {
    jgaapGUI gui = new jgaapGUI();
    
    ActionEvent event = new ActionEvent(gui.radioUnknown, ActionEvent.ACTION_PERFORMED, "unknownauthor");
    gui.actionPerformed(event);

    assertFalse(gui.currentAuthor.isEnabled());
    assertEquals("", gui.currentAuthor.getText());
  }

  @Test
  public void testActionPerformedImport() {
    jgaapGUI gui = new jgaapGUI();
    
    ActionEvent event = new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import");
    // TODO: set the file selection of chooseFileDialog

    gui.currentAuthor.setText("John Doe");

    gui.actionPerformed(event);

    // Assert that the document is added to driver
    // Assert that the currentAuthor is appended to listKnown if not empty
    // Assert that the file name is appended to listUnknown if currentAuthor is empty
  }

  // Continue implementing test cases for the remaining methods

}