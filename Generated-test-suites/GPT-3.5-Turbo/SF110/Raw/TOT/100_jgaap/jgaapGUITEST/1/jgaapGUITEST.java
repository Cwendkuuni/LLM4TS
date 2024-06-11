import static org.junit.Assert.*;
import org.junit.Test;


public class jgaapGUITest {
  
    @Test
    public void testActionListener() {
        jgaapGUI gui = new jgaapGUI();
        ActionEvent e1 = new ActionEvent(gui.buttonCanonicize, ActionEvent.ACTION_PERFORMED, "Canonicize");
        gui.actionPerformed(e1);
        // Check if the document canonicization process is started correctly
        
        ActionEvent e2 = new ActionEvent(gui.buttonEventSet, ActionEvent.ACTION_PERFORMED, "CreateEventSet");
        gui.actionPerformed(e2);
        // Check if the event set creation process is started correctly
        
        ActionEvent e3 = new ActionEvent(gui.buttonAnalyze, ActionEvent.ACTION_PERFORMED, "Analyze");
        gui.actionPerformed(e3);
        // Check if the statistical analysis process is started correctly
        
        ActionEvent e4 = new ActionEvent(gui.menuFileExit, ActionEvent.ACTION_PERFORMED, "Exit");
        gui.actionPerformed(e4);
        // Check if the program exits correctly
        
        // Add more test cases for other actions as needed
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // Check if the demo is loaded correctly
        // Add more test cases for specific demo scenarios as needed
    }
    
    // Add more test cases for other methods and functionalities in the jgaapGUI class as needed

}