import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class jgaapGUITest {
  
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        gui.currentAuthor.setText("Known Author");
        gui.currentAuthor.setEnabled(true);
        gui.actionPerformed(new ActionEvent(gui.menuFileExit, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Assert exit

        gui.currentAuthor.setEnabled(true);
        gui.actionPerformed(new ActionEvent(gui.radioKnown, ActionEvent.ACTION_PERFORMED, ""));
        // Assert currentAuthor enabled

        gui.currentAuthor.setEnabled(false);
        gui.actionPerformed(new ActionEvent(gui.radioUnknown, ActionEvent.ACTION_PERFORMED, ""));
        // Assert currentAuthor disabled
        
        gui.actionPerformed(new ActionEvent(gui.buttonLocal, ActionEvent.ACTION_PERFORMED, "Import"));
        // Assert import
        
        gui.paneOpts.setSelectedIndex(1);
        gui.buttonCanonicize.doClick();
        // Assert canonicize
        
        gui.paneOpts.setSelectedIndex(2);
        gui.buttonEventSet.doClick();
        // Assert createEventSet
        
        gui.paneOpts.setSelectedIndex(3);
        gui.buttonAnalyze.doClick();
        // Assert analyze
        
        gui.actionPerformed(new ActionEvent(gui.menuFileDemo, ActionEvent.ACTION_PERFORMED, "demo"));
        // Assert loadDemo
    }
}