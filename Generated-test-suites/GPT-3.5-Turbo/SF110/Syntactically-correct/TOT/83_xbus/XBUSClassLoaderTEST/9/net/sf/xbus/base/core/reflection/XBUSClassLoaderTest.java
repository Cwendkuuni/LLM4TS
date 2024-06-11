import org.junit.Test;
import java.net.URL;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case: Valid parent class loader is provided
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    
        // Edge case: Null parent class loader is provided
        classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Typical use case: Constants.XBUS_HOME is set
        System.setProperty("XBUS_HOME", "path/to/xbus/home");
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);

        // Error scenario: Constants.XBUS_HOME is not set
        System.clearProperty("XBUS_HOME");
        // Verify that Trace.error() is called and System.exit(1) is invoked
        XBUSClassLoader.createClassLoader(getClass().getClassLoader());

        // Verify that Trace.error() is called when creating classLoader with invalid URLs
        System.setProperty("XBUS_HOME", "path/to/xbus/home");
        URL[] urls = new URL[1];
        urls[0] = null;
        // Verify that Trace.error() is called and System.exit(1) is invoked
        XBUSClassLoader.createClassLoader(getClass().getClassLoader());
    }

    @Test
    public void testAddUrls() {
        // Typical use case: Valid directory name is provided
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "path/to/lib");
        assertFalse(urls.isEmpty());
    
        // Edge case: Empty directory name is provided
        urls.clear();
        XBUSClassLoader.addUrls(urls, "");
        assertTrue(urls.isEmpty());

        // Error scenario: Invalid directory name is provided
        urls.clear();
        XBUSClassLoader.addUrls(urls, "invalid/directory");
        assertTrue(urls.isEmpty());

        // Edge case: Null directory name is provided
        urls.clear();
        XBUSClassLoader.addUrls(urls, null);
        assertTrue(urls.isEmpty());
    }
}