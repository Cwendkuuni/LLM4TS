import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader_XBUS_HOMEIsNull_ExitWithError() {
        // Arrange
        String expectedErrorMessage = "XBUS_HOME has not been set!";
        
        // Act
        try {
            XBUSClassLoader.createClassLoader(null);
            fail("Expected System.exit(1) to be called");
        } catch (IllegalStateException ex) {
            // Assert
            assertEquals(expectedErrorMessage, ex.getMessage());
        }
    }
    
    @Test
    public void testCreateClassLoader_XBUS_HOMEIsValid_ReturnsClassLoader() {
        // Arrange
        ClassLoader parent = this.getClass().getClassLoader();
        
        // Act
        XBUSClassLoader result = XBUSClassLoader.createClassLoader(parent);
        
        // Assert
        assertNotNull(result);
        assertEquals(parent, result.getParent());
    }
    
    @Test
    public void testAddUrls_ValidDirectory_AddsUrlsToList() {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String validDirectoryName = "path/to/valid/directory";
        
        // Act
        XBUSClassLoader.addUrls(urls, validDirectoryName);
        
        // Assert
        assertFalse(urls.isEmpty());
        // Add more specific assertions for adding URLs to the list
    }
    
    @Test
    public void testAddUrls_InvalidDirectory_SkipsAddingUrls() {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String invalidDirectoryName = "path/to/invalid/directory";
        
        // Act
        XBUSClassLoader.addUrls(urls, invalidDirectoryName);
        
        // Assert
        assertTrue(urls.isEmpty());
    }
    
    // Add more test cases for the remaining methods in XBusClassLoader
    
}