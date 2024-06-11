import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader loader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(loader);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Edge case: Constants.XBUS_HOME is null
        Constants.XBUS_HOME = null;
        XBUSClassLoader loader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNull(loader);
        
        // Error scenario: MalformedURLException
        Constants.XBUS_HOME = "path/to/nonexistent";
        loader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNull(loader);
    }
    
    @Test
    public void testAddUrls() {
        // Typical use case
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "path/to/jars");
        assertFalse(urls.isEmpty());
        
        // Edge case: Empty directory
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/empty");
        assertTrue(urls.isEmpty());
    }
}