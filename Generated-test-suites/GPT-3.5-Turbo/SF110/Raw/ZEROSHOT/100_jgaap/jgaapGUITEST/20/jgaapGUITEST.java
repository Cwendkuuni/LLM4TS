import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        // Testing Exit action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit"));
        // Testing knownauthor action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "knownauthor"));
        // Testing unknownauthor action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "unknownauthor"));
        // Testing Import action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Import"));
        // Testing Canonicize action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Canonicize"));
        // Testing CreateEventSet action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CreateEventSet"));
        // Testing Analyze action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Analyze"));
        // Testing demo action
        gui.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "demo"));
    }
    
}