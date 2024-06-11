import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;
    
    @Before
    public void setup() {
        lagoonGUI = new LagoonGUI();
    }
    
    @Test
    public void testLagoonGUI() {
        assertNotNull(lagoonGUI);
        assertFalse(lagoonGUI.isVisible());
    }
    
    @Test
    public void testWindowOpened() {
        // TODO: Implement test case
    }
    
    @Test
    public void testWindowClosing() {
        // TODO: Implement test case
    }
    
    @Test
    public void testWindowClosed() {
        // TODO: Implement test case
    }
    
    @Test
    public void testWindowIconified() {
        // TODO: Implement test case
    }
    
    @Test
    public void testWindowDeiconified() {
        // TODO: Implement test case
    }
    
    @Test
    public void testWindowActivated() {
        // TODO: Implement test case
    }
    
    @Test
    public void testWindowDeactivated() {
        // TODO: Implement test case
    }
    
    @Test
    public void testBuild() {
        // TODO: Implement test case
    }
    
    @Test
    public void testExit() {
        // TODO: Implement test case
    }
    
    @Test
    public void testInit() {
        // TODO: Implement test case
    }
    
    @Test
    public void testLoadPropertyFile() {
        // TODO: Implement test case
    }
    
    @Test
    public void testLoadProperties() {
        // TODO: Implement test case
    }
    
    @Test
    public void testSavePropertyFile() {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetProperty() {
        // TODO: Implement test case
    }
    
    @Test
    public void testShowTime() {
        // TODO: Implement test case
    }
}