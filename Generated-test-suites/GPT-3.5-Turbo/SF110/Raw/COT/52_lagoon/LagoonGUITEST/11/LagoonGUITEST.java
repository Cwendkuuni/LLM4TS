import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setup() {
       lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testLagoonGUIConstructor() {
        assertNotNull(lagoonGUI);
    }

    @Test
    public void testBuild() {
        // Add test case here for build() method
    }

    @Test
    public void testExit() {
        // Add test case here for exit() method
    }

    @Test
    public void testInit() {
        // Add test case here for init() method
    }

    @Test
    public void testLoadPropertyFile() {
        // Add test case here for loadPropertyFile() method
    }

    @Test
    public void testLoadProperties() {
        // Add test case here for loadProperties() method
    }

    @Test
    public void testSavePropertyFile() {
        // Add test case here for savePropertyFile() method
    }

    @Test
    public void testGetProperty() {
        // Add test case here for getProperty() method
    }

    @Test
    public void testShowTime() {
        // Add test case here for showTime() method
    }

    @After
    public void tearDown() {
       lagoonGUI = null;
    }
    
    @Test
    public void testWindowOpened() {
        // Add test case here for windowOpened() method
    }
    
    @Test
    public void testWindowClosing() {
        // Add test case here for windowClosing() method
    }
    
    @Test
    public void testWindowClosed() {
        // Add test case here for windowClosed() method
    }
    
    @Test
    public void testWindowIconified() {
        // Add test case here for windowIconified() method
    }
    
    @Test
    public void testWindowDeiconified() {
        // Add test case here for windowDeiconified() method
    }
    
    @Test
    public void testWindowActivated() {
        // Add test case here for windowActivated() method
    }
    
    @Test
    public void testWindowDeactivated() {
        // Add test case here for windowDeactivated() method
    }
}