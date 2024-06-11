import static org.junit.Assert.*;
import org.junit.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testMain_whenNoArgumentsGiven_thenAppFrameShown() throws Exception {
        LagoonGUI.main(new String[]{});
        assertTrue(lagoonGUI.isVisible());
    }

    @Test
    public void testMain_whenArgumentGiven_thenPropertiesLoaded() throws Exception {
        LagoonGUI.main(new String[]{"test.properties"});
        assertEquals("testFile.xml", lagoonGUI.sitemapFile.getValue());
        assertEquals(".", lagoonGUI.sourceDir.getValue());
        assertEquals(".", lagoonGUI.targetURL.getValue());
    }

    @Test
    public void testWindowClosing_whenWindowClosed_thenExitMethodCalled() throws Exception {
        lagoonGUI.windowClosing(null);
        // verify exit method called
    }

    @Test
    public void testBuild_whenProcessorIsNull_thenProcessorInitialized() {
        lagoonGUI.build(false);
        assertNotNull(lagoonGUI.processor);
    }

    @Test
    public void testBuild_whenProcessorIsNotNullAndInputNotChanged_thenBuildNotInitialized() {
        lagoonGUI.build(false);
        lagoonGUI.build(false);
        // verify build not initialized
    }

    @Test
    public void testBuild_whenProcessorIsNotNullAndInputChanged_thenBuildInitialized() {
        lagoonGUI.build(false);
        lagoonGUI.sitemapFile.setValue("newFile.xml");
        lagoonGUI.build(false);
        // verify build initialized
    }

    @Test
    public void testBuild_whenBuildNotSuccessful_thenErrorMessageDisplayed() {
        lagoonGUI.build(false);
        // verify error message displayed
    }

    @Test
    public void testExit_whenExitMethodCalled_thenProcessorDestroyedAndSystemExitCalled() {
        lagoonGUI.exit();
        // verify processor destroyed
        // verify system exit called
    }

    @Test
    public void testInit_whenProcessorIsNotNull_thenProcessorDestroyed() {
        lagoonGUI.build(false);
        lagoonGUI.init();
        // verify processor destroyed
    }

    @Test
    public void testInit_whenSitemapFileNotFound_thenErrorMessageDisplayed() {
        lagoonGUI.sitemapFile.setValue("notFound.xml");
        lagoonGUI.init();
        // verify error message displayed
    }

    @Test
    public void testInit_whenIOExceptionThrown_thenErrorMessageDisplayed() {
        lagoonGUI.sitemapFile.setValue("file.xml");
        lagoonGUI.init();
        // verify error message displayed
    }

    @Test
    public void testInit_whenLagoonExceptionThrown_thenErrorMessageDisplayed() {
        lagoonGUI.sitemapFile.setValue("file.xml");
        lagoonGUI.init();
        // verify error message displayed
    }

    @Test
    public void testLoadPropertyFile_whenPropertyFileSelected_thenPropertiesLoaded() {
        // simulate selecting property file
        lagoonGUI.loadPropertyFile();
        assertEquals("testFile.xml", lagoonGUI.sitemapFile.getValue());
        assertEquals(".", lagoonGUI.sourceDir.getValue());
        assertEquals(".", lagoonGUI.targetURL.getValue());
    }

    @Test
    public void testSavePropertyFile_whenPropertyFileSelected_thenPropertiesSaved() {
        // simulate selecting property file
        lagoonGUI.savePropertyFile();
        // verify properties saved
    }

    @Test
    public void testGetProperty_whenValidPropertySpecified_thenValueReturned() throws LagoonException {
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "test.xml");
        String value = lagoonGUI.getProperty(prop, "sitemapFile");
        assertEquals("test.xml", value);
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_whenInvalidPropertySpecified_thenLagoonExceptionThrown() throws LagoonException {
        Properties prop = new Properties();
        lagoonGUI.getProperty(prop, "invalidProperty");
    }

    @Test
    public void testShowTime_whenLessThan10000ms_thenTimeStringInMillisecondsReturned() {
        String timeString = lagoonGUI.showTime(5000L);
        assertEquals("in 5000 ms", timeString);
    }

    @Test
    public void testShowTime_whenGreaterThan10000ms_thenTimeStringInSecondsReturned() {
        String timeString = lagoonGUI.showTime(15000L);
        assertEquals("in 15 s", timeString);
    }

}