import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    
    private XBUSClassLoader classLoader;
    
    @Before
    public void setup() throws MalformedURLException {
        URL[] urls = {
            new File("test.jar").toURI().toURL()
        };
        classLoader = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
    }

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader instance = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
        assertNotNull(instance);
        assertEquals(instance, classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader_NullParentClassLoader() {
        // Edge case: when parent class loader is null
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateClassLoader_NullXBUS_HOME() {
        // Error scenario: when XBUS_HOME is not set
        System.setProperty("XBUS_HOME", "");
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        // Typical use case
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test");
        assertFalse(urls.isEmpty());
    }

    @Test
    public void testAddUrls_EmptyUrlVector() {
        // Edge case: when the vector to add URLs to is empty
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "");
        assertTrue(urls.isEmpty());
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrls_InvalidUrl() {
        // Error scenario: when trying to add an invalid URL
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "/some/invalid/path");
    }

}