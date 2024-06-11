import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        boolean force = false;
        lagoonGUI.build(force);
        // add assertions here
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // add assertions here
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // add assertions here
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
        // add assertions here
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        File propertyFile = new File("test.properties");
        lagoonGUI.loadProperties(propertyFile);
        // add assertions here
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // add assertions here
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        String name = "property";
        String value = "value";
        prop.setProperty(name, value);
        String result = LagoonGUI.getProperty(prop, name);
        assertEquals(value, result);
    }

    @Test
    public void testShowTime() {
        long ms = 5000L;
        String result = LagoonGUI.showTime(ms);
        assertEquals("in 5 s", result);
    }
}