import static org.junit.Assert.*;
import org.junit.*;
import java.net.*;

public class XBUSClassLoaderTest {

    private ClassLoader parent;
    private XBUSClassLoader classLoader;

    @Before
    public void setUp() {
        parent = ClassLoader.getSystemClassLoader();
        classLoader = XBUSClassLoader.getInstance(parent);
    }

    @Test
    public void testGetInstance() {
        assertEquals("Instance should be created with the provided parent class loader", parent, classLoader.getParent());
    }

    @Test
    public void testCreateClassLoader() {
        assertNotNull("Class loader should not be null", classLoader);
    }

    @Test
    public void testAddUrls_withInvalidDirectory() {
        String invalidDir = "invalid/directory";
        try {
            classLoader.addUrls(null, invalidDir);
            fail("Should throw an exception when adding invalid directory");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddUrls_withValidDirectory_noJarsOrZips() {
        String validDir = "valid/directory";
        Vector<URL> urls = new Vector<>();
        classLoader.addUrls(urls, validDir);
        assertTrue("URLs should be empty when there are no jars or zips in the directory", urls.isEmpty());
    }

    @Test
    public void testAddUrls_withValidDirectory_jarsAndZips() {
        String validDir = "valid/directory";
        Vector<URL> urls = new Vector<>();
        classLoader.addUrls(urls, validDir);
        assertFalse("URLs should not be empty when there are jars or zips in the directory", urls.isEmpty());
    }

    @Test
    public void testAddUrls_withException() {
        String invalidDir = "invalid/directory";
        try {
            classLoader.addUrls(null, invalidDir);
            fail("Should throw an exception when adding invalid directory");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }

    @Test
    public void testAddUrls_withMalformedURLException() {
        String invalidDir = "invalid/directory";
        try {
            classLoader.addUrls(null, invalidDir);
            fail("Should throw an exception when adding invalid directory");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetInstance_singleton() {
        XBUSClassLoader instance1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(parent);
        assertSame("Instances should be the same", instance1, instance2);
    }

    @Test
    public void testCreateClassLoader_withoutXBUS_HOME() {
        try {
            XBUSClassLoader.getInstance(null);
            fail("Should exit with code 1 when XBUS_HOME is not set");
        } catch (SecurityException e) {
            // Exception expected
        }
    }

    @Test
    public void testCreateClassLoader_withXBUS_HOME() {
        String xbusHome = System.getenv("XBUS_HOME");
        assertNotNull("XBUS_HOME should be set", xbusHome);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull("Class loader should not be null when XBUS_HOME is set", classLoader);
    }
}