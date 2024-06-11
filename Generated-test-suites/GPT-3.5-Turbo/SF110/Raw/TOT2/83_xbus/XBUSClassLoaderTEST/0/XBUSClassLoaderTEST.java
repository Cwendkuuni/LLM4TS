import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
        
        // Test edge case with custom parent ClassLoader
        ClassLoader parent = new ClassLoader() {};
        XBUSClassLoader classLoaderWithParent = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoaderWithParent);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Test typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        // Test typical use case
        Vector<URL> urls = new Vector<URL>();
        XBUSClassLoader.addUrls(urls, "test/dir");
        assertEquals(0, urls.size());
        
        // Test edge case with non-existent directory
        Vector<URL> urls2 = new Vector<URL>();
        XBUSClassLoader.addUrls(urls2, "nonexistent/dir");
        assertEquals(0, urls2.size());
    }
}