import org.junit.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
        
        // Edge case: Passing null as parent ClassLoader
        XBUSClassLoader classLoaderNullParent = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoaderNullParent);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
        
        // Edge case: XBUS_HOME not set
        // This should trigger an error and exit the system
    }
    
    @Test
    public void testAddUrls() {
        // Typical use case
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/resources");
        assertEquals(1, urls.size());
        
        // Edge case: Directory containing files with different extensions
        Vector urlsMixedExtensions = new Vector();
        XBUSClassLoader.addUrls(urlsMixedExtensions, "test/resources/mixed");
        assertEquals(2, urlsMixedExtensions.size());
        
        // Error scenario: Malformed URL
        // This should trigger an error and exit the system
    }
}