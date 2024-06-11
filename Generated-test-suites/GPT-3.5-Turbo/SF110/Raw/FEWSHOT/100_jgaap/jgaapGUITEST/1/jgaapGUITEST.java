import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class jgaapGUITest {
    
    private jgaapGUI jgaapGUI;

    @Before
    public void setUp() {
        jgaapGUI = new jgaapGUI();
    }

    @Test
    public void testActionPerformed() {
        // Test Exit Command
        ActionEvent exitEvent = new ActionEvent(null, 0, "Exit");
        jgaapGUI.actionPerformed(exitEvent);
        //Assert statement

        // Test knownauthor Command
        ActionEvent knownAuthorEvent = new ActionEvent(null, 0, "knownauthor");
        jgaapGUI.actionPerformed(knownAuthorEvent);
        //Assert statement

        // Test unknownauthor Command
        ActionEvent unknownAuthorEvent = new ActionEvent(null, 0, "unknownauthor");
        jgaapGUI.actionPerformed(unknownAuthorEvent);
        //Assert statement

        // Test Import Command
        ActionEvent importEvent = new ActionEvent(null, 0, "Import");
        jgaapGUI.actionPerformed(importEvent);
        //Assert statement

        // Test Canonicize Command
        ActionEvent canonicizeEvent = new ActionEvent(null, 0, "Canonicize");
        jgaapGUI.actionPerformed(canonicizeEvent);
        //Assert statement

        // Test CreateEventSet Command
        ActionEvent createEventSetEvent = new ActionEvent(null, 0, "CreateEventSet");
        jgaapGUI.actionPerformed(createEventSetEvent);
        //Assert statement

        // Test Analyze Command
        ActionEvent analyzeEvent = new ActionEvent(null, 0, "Analyze");
        jgaapGUI.actionPerformed(analyzeEvent);
        //Assert statement

        // Test demo Command
        ActionEvent demoEvent = new ActionEvent(null, 0, "demo");
        jgaapGUI.actionPerformed(demoEvent);
        //Assert statement

        // Test other commands
        ActionEvent otherEvent = new ActionEvent(null, 0, "other");
        jgaapGUI.actionPerformed(otherEvent);
        //Assert statement
    }

    //Write test cases for all other methods in jgaapGUI class

}