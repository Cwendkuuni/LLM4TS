import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
        // Add assertions for the expected behavior of the build() method
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // Add assertions for the expected behavior of the exit() method
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // Add assertions for the expected behavior of the init() method
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
        // Add assertions for the expected behavior of the loadPropertyFile() method
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(null);
        // Add assertions for the expected behavior of the loadProperties() method
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // Add assertions for the expected behavior of the savePropertyFile() method
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        // Set properties in the prop object
        String value = LagoonGUI.getProperty(prop, "property");
        // Add assertions for the expected behavior of the getProperty() method
    }

    @Test
    public void testShowTime() {
        long ms = 1000L;
        String time = LagoonGUI.showTime(ms);
        // Add assertions for the expected behavior of the showTime() method
    }
}