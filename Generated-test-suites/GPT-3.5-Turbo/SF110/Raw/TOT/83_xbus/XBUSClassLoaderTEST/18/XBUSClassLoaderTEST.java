import org.junit.*;
import java.net.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull("getInstance() returned null", classLoader);
    }

    @Test
    public void testAddUrls_validDirectory() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "valid/directory");
        assertFalse("URLs vector is still empty", urls.isEmpty());
    }

    @Test
    public void testAddUrls_invalidDirectory() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "invalid/directory");
        assertTrue("URLs vector is not empty", urls.isEmpty());
    }

    @Test
    public void testAddUrls_directoryWithNoJarsOrZips() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "directory/without/jars");
        assertTrue("URLs vector is not empty", urls.isEmpty());
    }

    @Test
    public void testCreateClassLoader_xbusHomeNotSet() throws IOException {
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        try {
            classLoader.loadClass("TestClass");
            fail("Expected XBUS_HOME not set error");
        } catch (ClassNotFoundException e) {
            assertEquals("XBUS_HOME has not been set!", e.getMessage());
        }
    }

    @Test
    public void testCreateClassLoader_xbusHomeSet() {
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        assertNotNull("mClassLoader is null", classLoader.mClassLoader);
    }
}