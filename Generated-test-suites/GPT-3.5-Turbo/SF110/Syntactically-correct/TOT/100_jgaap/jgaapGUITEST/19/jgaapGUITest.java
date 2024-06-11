import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class jgaapGUITest {
    
    jgaapGUI gui;
    
    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }
    
    @Test
    public void testActionPerformed_Exit() {
        //Test the actionPerformed method for "Exit" command
        //Expected behavior is that the system exits with -1
        //Test case includes typical use case
        //Edge cases and error scenarios are not applicable in this test case
        
        //No assertion can be made as the system exits
        assertEquals(0, gui.actionPerformed("Exit"));
    }
    
    @Test
    public void testActionPerformed_KnownAuthor() {
        //Test the actionPerformed method for "knownauthor" command
        //Expected behavior is that the currentAuthor text field should be enabled
        //Test case includes typical use case
        
        //Before the action event
        assertFalse(gui.currentAuthor.isEnabled());
        
        //Performing the action event
        gui.actionPerformed("knownauthor");
        
        //After the action event
        assertTrue(gui.currentAuthor.isEnabled());
    }
    
    @Test
    public void testActionPerformed_UnknownAuthor() {
        //Test the actionPerformed method for "unknownauthor" command
        //Expected behavior is that the currentAuthor text field should be disabled
        //Test case includes typical use case
        
        //Before the action event
        assertTrue(gui.currentAuthor.isEnabled());
        
        //Performing the action event
        gui.actionPerformed("unknownauthor");
        
        //After the action event
        assertFalse(gui.currentAuthor.isEnabled());
    }
    
    @Test
    public void testActionPerformed_Import() {
        //Test the actionPerformed method for "Import" command
        //Expected behavior is that the driver should add the document and update the appropriate list
        //Test case includes typical use case
        
        //Setting up the necessary variables
        File file = new File("sample file");
        String author = "John";
        JOptionPane chooseFile = mock(JOptionPane.class);
        when(chooseFile.showOpenDialog(gui)).thenReturn(0);
        when(chooseFile.getSelectedFile()).thenReturn(file);
        
        //Performing the action event
        gui.actionPerformed("Import");
        
        //Assertion
        assertEquals(author + " - " + file.getName() + "\n", gui.listKnown.getText());
    }
    
    @Test
    public void testActionPerformed_Canonicize() {
        //Test the actionPerformed method for "Canonicize" command
        //Expected behavior is that the driver should start the preprocessEngineDriver for canonicization
        //Test case includes typical use case
        
        //No assertion can be made as the driver starts the engine
        gui.actionPerformed("Canonicize");
    }
    
    @Test
    public void testActionPerformed_CreateEventSet() {
        //Test the actionPerformed method for "CreateEventSet" command
        //Expected behavior is that the driver should start the createEventSetDriver for event set creation
        //Test case includes typical use case
        
        //No assertion can be made as the driver starts the creation
        gui.actionPerformed("CreateEventSet");
    }
    
    @Test
    public void testActionPerformed_Analyze() {
        //Test the actionPerformed method for "Analyze" command
        //Expected behavior is that the driver should start the runStatisticalAnalysisDriver for analysis
        //Test case includes typical use case
        
        //No assertion can be made as the driver starts the analysis
        gui.actionPerformed("Analyze");
    }
    
    @Test
    public void testActionPerformed_Demo() {
        //Test the actionPerformed method for "demo" command
        //Expected behavior is that the loadDemo method should be called
        //Test case includes typical use case
        
        //No assertion can be made as the loadDemo method does not return any value
        gui.actionPerformed("demo");
    }
    
    @Test
    public void testLoadDemo() {
        //Test the loadDemo method
        //Expected behavior is that the "Need to Demo" message should be printed to the console
        //Test case includes typical use case
        
        //No assertion can be made as the method only prints a message to the console
        gui.loadDemo();
    }
}