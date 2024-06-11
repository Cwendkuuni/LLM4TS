import org.junit.Test;
import org.junit.Assert;

import java.awt.event.ActionEvent;

import static junit.framework.TestCase.assertEquals;

public class TestJgaapGUI {

@Test
public void testInitComponents() {
	jgaapGUI gui = new jgaapGUI();// check if all components are properly initialized

	}

	@Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent event = new ActionEvent(gui, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(event);
        // Assert that the system exited with status code -1
        // (this assumes that you have a way to capture the exit status)
        
        // Test other cases of actionPerformed() by creating different ActionEvents
        // and asserting that the expected changes have been made to the GUI and/or the
        // console output.
    }


    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
    }
}

