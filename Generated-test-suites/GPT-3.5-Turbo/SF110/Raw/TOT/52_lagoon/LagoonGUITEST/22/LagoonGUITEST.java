import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {
    
    private LagoonGUI lagoonGUI;
    
    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }
    
    @Test
    public void testBuild_NoChanges_Success() {
        // Typical use case
        lagoonGUI.build(false);
        // Assert that statusLabel is now "Ready"
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testBuild_Force_BuildSuccess_Success() {
        // Typical use case
        lagoonGUI.build(true);
        // Assert that progressArea contains "--- Build finished successfully"
        assertTrue(lagoonGUI.progressArea.getText().contains("--- Build finished successfully"));
    }
    
    @Test
    public void testBuild_InvalidFile_ExceptionThrown() {
        // Error scenario: sitemap file not found
        lagoonGUI.sitemapFile.setValue("invalid_file.xml");
        lagoonGUI.build(false);
        // Assert that progressArea contains "File not found: invalid_file.xml"
        assertTrue(lagoonGUI.progressArea.getText().contains("File not found: invalid_file.xml"));
    }
    
    @Test
    public void testExit_Success() {
        // Typical use case
        lagoonGUI.exit();
        // Assert that System.exit(0) is called
        // This can be done using a library like System Rules or PowerMock
    }
    
    @Test
    public void testInit_SitemapFileChanged_Success() {
        // Edge case: Sitemap file has changed
        lagoonGUI.sitemapFile.setValue("new_sitemap.xml");
        lagoonGUI.init();
        // Assert that statusLabel is now "Initializing..."
        assertEquals("Initializing...", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testLoadPropertyFile_Success() {
        // Typical use case
        lagoonGUI.loadPropertyFile();
        // Assert that sitemapFile, sourceDir, and targetURL values are loaded correctly
        assertEquals("expected_sitemap.xml", lagoonGUI.sitemapFile.getValue());
        assertEquals("expected_source_dir", lagoonGUI.sourceDir.getValue());
        assertEquals("expected_target_url", lagoonGUI.targetURL.getValue());
    }
    
    @Test
    public void testSavePropertyFile_Success() {
        // Typical use case
        lagoonGUI.savePropertyFile();
        // Assert that statusLabel is now "Properties saved"
        assertEquals("Properties saved", lagoonGUI.statusLabel.getText());
    }
}