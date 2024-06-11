import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Test typical case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Test typical case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
        
        // Test edge case when Constants.XBUS_HOME is null
        Constants.XBUS_HOME = null;
        try {
            XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
            fail("Expected System.exit(1) for null XBUS_HOME");
        } catch (SecurityException ignored) {
            // Expected exception
        }
    }
    
    @Test
    public void testAddUrls() {
        // Test typical case
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "/test/lib");
        assertFalse(urls.isEmpty());
        
        // Test edge case when dirName does not exist
        Vector emptyUrls = new Vector();
        XBUSClassLoader.addUrls(emptyUrls, "/non-existent-dir");
        assertTrue(emptyUrls.isEmpty());
        
        // Test edge case when jars are not files
        Vector nonFileUrls = new Vector();
        XBUSClassLoader.addUrls(nonFileUrls, System.getProperty("java.home")); // System property is a directory
        assertTrue(nonFileUrls.isEmpty());
        
        // Test exception handling case
        Vector exceptionUrls = new Vector();
        XBUSClassLoader.addUrls(exceptionUrls, "/test/invalid");
        assertTrue(exceptionUrls.isEmpty());
    }
}