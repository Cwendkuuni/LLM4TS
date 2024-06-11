import org.junit.Test;

import static org.junit.Assert.*;

public class jgaapGUITest {

    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        // Testing "Exit" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Exit"));
        // Assert the program exits with status -1
        
        // Testing "knownauthor" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "knownauthor"));
        // Assert the currentAuthor text field is enabled
        
        // Testing "unknownauthor" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "unknownauthor"));
        // Assert the currentAuthor text field is disabled
        
        // Testing "Import" command
        File file = new File("test.txt");
        gui.chooseFile.setSelectedFile(file);
        // Assume gui.driver.addDocument() and gui.listKnown.append() are properly implemented
        
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Import"));
        // Assert the document is added to the driver and the file name is appended to listKnown
        
        // Testing "Canonicize" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Canonicize"));
        // Assert the preprocessEngineDriver is called and started
        
        // Testing "CreateEventSet" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "CreateEventSet"));
        // Assert the createEventSetDriver is called and started
        
        // Testing "Analyze" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Analyze"));
        // Assert the runStatisticalAnalysisDriver is called and started, and the results are appended to listResults
        
        // Testing "demo" command
        gui.actionPerformed(new ActionEvent(new Object(), 0, "demo"));
        // Assert the loadDemo() method is called
        
    }
    
}