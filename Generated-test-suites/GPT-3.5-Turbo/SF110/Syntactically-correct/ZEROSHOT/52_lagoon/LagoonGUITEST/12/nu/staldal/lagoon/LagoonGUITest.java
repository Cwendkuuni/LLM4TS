import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);

        // Assert statements
        // ...
    }

    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();

        // Assert statements
        // ...
    }

    @Test
    public void testInit() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();

        // Assert statements
        // ...
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();

        // Assert statements
        // ...
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadProperties(new File("test.properties"));

        // Assert statements
        // ...
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();

        // Assert statements
        // ...
    }

    @Test
    public void testGetProperty() {
        LagoonGUI gui = new LagoonGUI();
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        String result = gui.getProperty(prop, "key");

        // Assert statements
        assertEquals("value", result);
    }

    @Test
    public void testShowTime() {
        LagoonGUI gui = new LagoonGUI();
        long ms = 5000;
        String result = gui.showTime(ms);

        // Assert statements
        assertEquals("in 5 s", result);
    }
}