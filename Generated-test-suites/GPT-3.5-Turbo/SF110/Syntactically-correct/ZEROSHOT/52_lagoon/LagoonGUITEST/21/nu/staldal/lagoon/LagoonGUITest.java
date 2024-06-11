import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testLagoonGUIConstructor() {
        LagoonGUI gui = new LagoonGUI();
        assertNotNull(gui);
    }

    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);
        // TODO: Add assertions
    }

    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
        // TODO: Add assertions
    }

    @Test
    public void testInit() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();
        // TODO: Add assertions
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
        // TODO: Add assertions
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadProperties(new File("test.properties"));
        // TODO: Add assertions
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
        // TODO: Add assertions
    }

    @Test
    public void testGetProperty() {
        LagoonGUI gui = new LagoonGUI();
        Properties prop = new Properties();
        prop.setProperty("name", "value");
        try {
            String result = gui.getProperty(prop, "name");
            assertEquals("value", result);
        } catch (LagoonException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        LagoonGUI gui = new LagoonGUI();
        String result = gui.showTime(5000);
        assertEquals("in 5 s", result);
    }
}