import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Edge case: XBUS_HOME is not set
        try {
            System.setProperty("XBUS_HOME", null);
            ClassLoader parent = getClass().getClassLoader();
            XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
            assertNull(classLoader);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testAddUrls() {
        // Error scenario: Malformed URL
        try {
            Vector urls = new Vector();
            String dirName = "invalid/path";
            XBUSClassLoader.addUrls(urls, dirName);
            fail("MalformedURLException not thrown");
        } catch (MalformedURLException e) {
            assertTrue(true);
        }
    }
}