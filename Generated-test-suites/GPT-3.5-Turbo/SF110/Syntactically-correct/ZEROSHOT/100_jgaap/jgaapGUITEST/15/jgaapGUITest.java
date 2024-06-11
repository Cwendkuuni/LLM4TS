import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {
  private jgaapGUI gui;

  @Before
  public void setUp() {
    gui = new jgaapGUI();
  }

  @Test
  public void testActionPerformed() {
    assertEquals(0, gui.listKnown.getText().length());
    gui.radioKnown.doClick();
    gui.currentAuthor.setText("Author1");
    gui.buttonLocal.doClick();
    assertEquals("Author1 - Local Document\n", gui.listKnown.getText());
    assertEquals("", gui.currentAuthor.getText());
    gui.radioUnknown.doClick();
    gui.buttonLocal.doClick();
    assertEquals("Local Document\n", gui.listUnknown.getText());
    assertEquals("", gui.currentAuthor.getText());

    assertEquals("", gui.listResults.getText());
    gui.buttonCanonicize.doClick();
    assertEquals("D", gui.progressCanonicize.getString());
    gui.buttonEventSet.doClick();
    assertEquals("D", gui.progressEventSet.getString());
    gui.buttonAnalyze.doClick();
    assertEquals("D", gui.progressAnalysis.getString());

    gui.menuFileDemo.doClick();
    assertEquals("Need to Demo", gui.listResults.getText());
  }
}