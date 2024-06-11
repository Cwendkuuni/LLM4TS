import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testLoadButtonAction() {
        // Test loading properties file
        LagoonGUI gui = new LagoonGUI();
        // Set up mock property file
        File propertyFile = createMockPropertyFile();
        // Call loadProperties() method
        gui.loadProperties(propertyFile);
        // Verify that properties were loaded correctly
        assertEquals("Sitemap file value should be", "mockSitemapFile", gui.sitemapFile.getValue());
        assertEquals("Source directory value should be", "mockSourceDir", gui.sourceDir.getValue());
        assertEquals("Target URL value should be", "mockTargetURL", gui.targetURL.getValue());
        assertNull("Password should be null", gui.password);
    }

    @Test
    public void testSaveButtonAction() {
        // Test saving properties file
        LagoonGUI gui = new LagoonGUI();
        // Set up mock property file
        File propertyFile = createMockPropertyFile();
        // Set up mock values for input components
        gui.sitemapFile.setValue("mockSitemapFile");
        gui.sourceDir.setValue("mockSourceDir");
        gui.targetURL.setValue("mockTargetURL");
        gui.password = "mockPassword";
        // Call savePropertyFile() method
        gui.savePropertyFile();
        // Verify that properties file was created and saved correctly
        File savedPropertyFile = new File(propertyFile.getAbsolutePath());
        assertTrue("Saved property file should exist", savedPropertyFile.exists());
        // Verify the content of the saved property file
        Properties savedProperties = getPropertiesFromFile(savedPropertyFile);
        assertEquals("Sitemap file value should be", "mockSitemapFile", savedProperties.getProperty("sitemapFile"));
        assertEquals("Source directory value should be", "mockSourceDir", savedProperties.getProperty("sourceDir"));
        assertEquals("Target URL value should be", "mockTargetURL", savedProperties.getProperty("targetURL"));
        assertNull("Password should be null", savedProperties.getProperty("password"));
    }
  
    @Test
    public void testBuildButtonAction() {
        // Test build button action
        LagoonGUI gui = new LagoonGUI();
        // Set up mock values for input components
        gui.sitemapFile.setValue("mockSitemapFile");
        gui.sourceDir.setValue("mockSourceDir");
        gui.targetURL.setValue("mockTargetURL");
        gui.password = "mockPassword";
        // Mock the LagoonProcessor
        LagoonProcessor mockProcessor = mock(LagoonProcessor.class);
        gui.processor = mockProcessor;
        // Call build() method
        gui.build(false);
        // Verify that necessary methods were called on the mock LagoonProcessor object
        verify(mockProcessor).build(false);
        verify(mockProcessor).destroy();
        // Verify that necessary components were enabled/disabled
        assertFalse("Load button should be disabled", gui.loadButton.isEnabled());
        assertFalse("Save button should be disabled", gui.saveButton.isEnabled());
        assertFalse("Build button should be disabled", gui.buildButton.isEnabled());
        assertFalse("Force button should be disabled", gui.forceButton.isEnabled());
        assertFalse("Sitemap file input component should be disabled", gui.sitemapFile.isEnabled());
        assertFalse("Source directory input component should be disabled", gui.sourceDir.isEnabled());
        assertFalse("Target URL input component should be disabled", gui.targetURL.isEnabled());
        // Verify that status label is updated correctly
        assertEquals("Status label should be 'Building...'", "Building...", gui.statusLabel.getText());
    }
  
    @Test
    public void testExitButtonAction() {
        // Test exit button action
        LagoonGUI gui = new LagoonGUI();
        // Mock the LagoonProcessor
        LagoonProcessor mockProcessor = mock(LagoonProcessor.class);
        gui.processor = mockProcessor;
        // Call exit() method
        gui.exit();
        // Verify that necessary methods were called on the mock LagoonProcessor object
        verify(mockProcessor).destroy();
    }

    // Helper methods

    private File createMockPropertyFile() {
        // Create a mock property file for testing purposes
        File propertyFile = new File("mockPropertyFile.properties");
        try {
            FileWriter writer = new FileWriter(propertyFile);
            writer.write("sitemapFile=mockSitemapFile\n");
            writer.write("sourceDir=mockSourceDir\n");
            writer.write("targetURL=mockTargetURL\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyFile;
    }

    private Properties getPropertiesFromFile(File file) {
        // Retrieve properties from a file
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}