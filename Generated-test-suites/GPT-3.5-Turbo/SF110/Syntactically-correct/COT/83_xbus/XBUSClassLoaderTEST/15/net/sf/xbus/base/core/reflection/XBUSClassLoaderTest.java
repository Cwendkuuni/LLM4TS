
import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test if getInstance method returns an instance of XBUSClassLoader
        // Test with a null parent ClassLoader
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
        assertEquals(XBUSClassLoader.class, classLoader.getClass());
    }

    @Test
    public void testCreateClassLoader() {
        // Test if createClassLoader method creates an instance of XBUSClassLoader
        // Test with a null parent ClassLoader
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
        assertEquals(XBUSClassLoader.class, classLoader.getClass());
    }

    @Test(expected = Error.class)
    public void testCreateClassLoaderWithoutXBUS_HOME() {
        // Test if createClassLoader method throws an Error when Constants.XBUS_HOME is null
        XBUSClassLoader.createClassLoader(getClass().getClassLoader());
    }

    @Test
    public void testAddUrls() {
        // Test if addUrls method adds URLs to the vector
        // Test with existing directory
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/existing/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithInvalidDirectory() {
        // Test if addUrls method throws MalformedURLException for an invalid directory
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/non/existing/directory";
        XBUSClassLoader.addUrls(urls, dirName);
    }
}

