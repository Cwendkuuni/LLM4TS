import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest
{
    LagoonGUI lagoonGUI;

    @Before
    public void setUp()
    {
        lagoonGUI = new LagoonGUI();
    }
    
    @Test
    public void testConstructor()
    {
        assertNotNull(lagoonGUI);
        assertEquals("Lagoon", lagoonGUI.getTitle());
        assertTrue(lagoonGUI.windowListeners.contains(lagoonGUI));
        assertNotNull(lagoonGUI.buttonPanel);
        assertNotNull(lagoonGUI.inputPanel);
        assertNotNull(lagoonGUI.centerPanel);
        assertNotNull(lagoonGUI.loadButton);
        assertNotNull(lagoonGUI.saveButton);
        assertNotNull(lagoonGUI.buildButton);
        assertNotNull(lagoonGUI.forceButton);
        assertNotNull(lagoonGUI.exitButton);
        assertNotNull(lagoonGUI.sitemapFile);
        assertNotNull(lagoonGUI.sourceDir);
        assertNotNull(lagoonGUI.targetURL);
        assertNull(lagoonGUI.password);
        assertNotNull(lagoonGUI.statusLabel);
        assertNotNull(lagoonGUI.progressArea);
        assertNull(lagoonGUI.processor);
        assertEquals(0L, lagoonGUI.sitemapLastModified);
    }

    @Test
    public void testWindowClosing()
    {
        lagoonGUI.windowClosing(null);
        assertTrue(lagoonGUI.isClosing());
    }
    
    @Test
    public void testWindowClosed()
    {
        lagoonGUI.windowClosed(null);
        assertFalse(lagoonGUI.isClosing());
    }
    
    @Test
    public void testBuild()
    {
        lagoonGUI.build(false);
        // Test that all necessary components are disabled during build
        assertFalse(lagoonGUI.loadButton.isEnabled());
        assertFalse(lagoonGUI.saveButton.isEnabled());
        assertFalse(lagoonGUI.buildButton.isEnabled());
        assertFalse(lagoonGUI.forceButton.isEnabled());
        assertFalse(lagoonGUI.sitemapFile.isEnabled());
        assertFalse(lagoonGUI.sourceDir.isEnabled());
        assertFalse(lagoonGUI.targetURL.isEnabled());
        // Test that progressArea and statusLabel are updated correctly
        assertEquals("--- Start build ---\n", lagoonGUI.progressArea.getText());
        assertEquals("Building...", lagoonGUI.statusLabel.getText());
        // TODO: Test the actual build process
        // Test that components are enabled again after build
        assertTrue(lagoonGUI.loadButton.isEnabled());
        assertTrue(lagoonGUI.saveButton.isEnabled());
        assertTrue(lagoonGUI.buildButton.isEnabled());
        assertTrue(lagoonGUI.forceButton.isEnabled());
        assertTrue(lagoonGUI.sitemapFile.isEnabled());
        assertTrue(lagoonGUI.sourceDir.isEnabled());
        assertTrue(lagoonGUI.targetURL.isEnabled());
        // Test that statusLabel is updated to "Ready"
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testInit()
    {
        lagoonGUI.init();
        // Test that processor is initialized correctly
        assertNotNull(lagoonGUI.processor);
        // TODO: Test the actual initialization process
        // Test that statusLabel is updated to "Initializing..."
        assertEquals("Initializing...", lagoonGUI.statusLabel.getText());
        // Test that processor is set to null
        assertNull(lagoonGUI.processor);        
        // Test that statusLabel is updated to "Not initialized"
        assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testLoadPropertyFile()
    {
        // TODO: Implement test for loadPropertyFile()
    }

    @Test
    public void testLoadProperties()
    {
        // TODO: Implement test for loadProperties()
    }

    @Test
    public void testSavePropertyFile()
    {
        // TODO: Implement test for savePropertyFile()
    }

    @Test
    public void testGetProperty()
    {
        // TODO: Implement test for getProperty()
    }

    @Test
    public void testShowTime()
    {
        // Test for ms less than 10000
        long ms = 5000L;
        String result = LagoonGUI.showTime(ms);
        assertEquals("in 5000 ms", result);
        // Test for ms greater than 10000
        ms = 15000L;
        result = LagoonGUI.showTime(ms);
        assertEquals("in 15 s", result);
    }

}