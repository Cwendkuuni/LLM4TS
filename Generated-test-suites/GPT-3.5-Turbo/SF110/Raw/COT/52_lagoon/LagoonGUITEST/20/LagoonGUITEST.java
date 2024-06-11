
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.show();
    }

    @Test
    public void testBuild() {
        lagoonGUI.build(false);
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
        lagoonGUI.loadProperties(new File("test.properties"));
    }

    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
    }
}
