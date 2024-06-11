import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);
        // Add assertion statements
    }

    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
        // Add assertion statements
    }

    @Test
    public void testInit() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();
        // Add assertion statements
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
        // Add assertion statements
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadProperties(null);
        // Add assertion statements
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
        // Add assertion statements
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        String result = LagoonGUI.getProperty(prop, "test");
        // Add assertion statements
    }

    @Test
    public void testShowTime() {
        long time = 5000;
        String result = LagoonGUI.showTime(time);
        // Add assertion statements
    }
}