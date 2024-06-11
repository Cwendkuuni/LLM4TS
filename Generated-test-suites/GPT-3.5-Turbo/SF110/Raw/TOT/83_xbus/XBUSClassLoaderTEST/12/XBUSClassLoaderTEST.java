import org.junit.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    
    private XBUSClassLoader classLoader;
    
    @Before
    public void setUp() {
        classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
    }
    
    @Test
    public void testGetInstance() {
        assertNotNull("Instance is not null", classLoader);
        assertEquals("Parent class loader is correct", ClassLoader.getSystemClassLoader(), classLoader.getParent());
    }
    
    @Test
    public void testCreateClassLoaderWithoutXBUSHome() {
        // Arrange
        System.setProperty("XBUS_HOME", "");
        
        // Act
        XBUSClassLoader createdClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        
        // Assert
        assertNull("Class loader should be null when XBUS_HOME is not set", createdClassLoader);
    }
    
    @Test
    public void testCreateClassLoaderWithValidXBUSHome() {
        // Arrange
        System.setProperty("XBUS_HOME", "/path/to/xbus/home");
        
        // Act
        XBUSClassLoader createdClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        
        // Assert
        assertNotNull("Class loader should be created when XBUS_HOME is set", createdClassLoader);
        
        // Additional assertions for URL array contents
        // ...
    }
    
    @Test
    public void testAddUrlsWithValidDirectory() {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String directory = "/path/to/lib";
        
        // Act
        XBUSClassLoader.addUrls(urls, directory);
        
        // Assert
        assertFalse("URLs vector should not be empty", urls.isEmpty());
        
        // Additional assertions for URL contents
        // ...
    }
    
    @Test
    public void testAddUrlsWithInvalidDirectory() {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String directory = "/invalid/directory";
        
        // Act
        XBUSClassLoader.addUrls(urls, directory);
        
        // Assert
        assertTrue("URLs vector should be empty when directory is invalid", urls.isEmpty());
    }
    
    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedURL() throws MalformedURLException {
        // Arrange
        Vector<URL> urls = new Vector<>();
        String directory = "/path/to/malformed/url";
        
        // Act
        XBUSClassLoader.addUrls(urls, directory);
        
        // Assert (Exception expected)
    }
}