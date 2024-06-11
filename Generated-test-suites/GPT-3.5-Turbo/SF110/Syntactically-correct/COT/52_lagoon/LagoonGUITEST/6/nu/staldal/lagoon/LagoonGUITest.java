import org.junit.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testMain() {
        try {
            LagoonGUI.main(new String[]{});
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLagoonGUI() {
        Assert.assertNotNull(lagoonGUI);
    }

    @Test
    public void testWindowOpened() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_OPENED);
        lagoonGUI.windowOpened(windowEvent);
    }

    @Test
    public void testWindowClosing() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_CLOSING);
        lagoonGUI.windowClosing(windowEvent);
    }

    @Test
    public void testWindowClosed() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_CLOSED);
        lagoonGUI.windowClosed(windowEvent);
    }

    @Test
    public void testWindowIconified() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_ICONIFIED);
        lagoonGUI.windowIconified(windowEvent);
    }

    @Test
    public void testWindowDeiconified() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_DEICONIFIED);
        lagoonGUI.windowDeiconified(windowEvent);
    }

    @Test
    public void testWindowActivated() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_ACTIVATED);
        lagoonGUI.windowActivated(windowEvent);
    }

    @Test
    public void testWindowDeactivated() {
        WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_DEACTIVATED);
        lagoonGUI.windowDeactivated(windowEvent);
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

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testProperty", "testValue");
        try {
            String value = LagoonGUI.getProperty(prop, "testProperty");
            Assert.assertEquals("testValue", value);
        } catch (LagoonException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        String time = LagoonGUI.showTime(5000);
        Assert.assertEquals("in 5000 ms", time);
     
        time = LagoonGUI.showTime(15000);
        Assert.assertEquals("in 15 s", time);
    }
}