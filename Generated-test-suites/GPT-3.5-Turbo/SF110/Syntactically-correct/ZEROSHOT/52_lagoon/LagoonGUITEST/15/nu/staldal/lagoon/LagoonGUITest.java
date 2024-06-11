import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
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
        lagoonGUI.loadProperties();
        // add assertions here
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // add assertions here
    }

    @Test
    public void testGetProperty() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.getProperty();
        // add assertions here
    }

    @Test
    public void testShowTime() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.showTime();
        // add assertions here
    }
}