import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class jgaapGUITest {
    private jgaapGUI gui;

    @Before
    public void setUp() {
        gui = new jgaapGUI();
    }

    @Test
    public void testConstructor() {
        assertNotNull(gui);
    }

    @Test
    public void testActionPerformed_Exit() {
        // Add test here
    }

    @Test
    public void testActionPerformed_KnownAuthor() {
        // Add test here
    }

    @Test
    public void testActionPerformed_UnknownAuthor() {
        // Add test here
    }

    @Test
    public void testActionPerformed_Import() {
        // Add test here
    }

    @Test
    public void testActionPerformed_Canonicize() {
        // Add test here
    }

    @Test
    public void testActionPerformed_CreateEventSet() {
        // Add test here
    }

    @Test
    public void testActionPerformed_Analyze() {
        // Add test here
    }

    @Test
    public void testActionPerformed_Demo() {
        // Add test here
    }

    @Test
    public void testLoadDemo() {
        // Add test here
    }
}