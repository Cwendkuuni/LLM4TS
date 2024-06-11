import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testBuild() {
        // Typical use case
        lagoonGUI.build(false);

        // Edge case - force build
        lagoonGUI.build(true);
    }

    @Test
    public void testExit() {
        lagoonGUI.exit();
    }

    @Test
    public void testInit() {
        lagoonGUI.init();
    }

    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
    }

    @Test
    public void testLoadProperties() {
        // Typical use case
        lagoonGUI.loadProperties(new File("test.properties"));

        // Edge case - property file not found
        lagoonGUI.loadProperties(new File("non_existing.properties"));
    }

    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
    }

    @Test
    public void testGetProperty() {
        // Typical use case
        Properties prop = new Properties();
        prop.setProperty("test", "value");
        String result = LagoonGUI.getProperty(prop, "test");
        assertEquals("value", result);

        // Edge case - property not found
        try {
            LagoonGUI.getProperty(prop, "not_found");
            fail("Expected LagoonException");
        } catch (LagoonException e) {
            // Expected
        }
    }

    @Test
    public void testShowTime() {
        // Typical use case
        String result = LagoonGUI.showTime(5000);
        assertEquals("in 5 s", result);

        // Edge case - time less than 10 seconds
        result = LagoonGUI.showTime(500);
        assertEquals("in 500 ms", result);
    }
}