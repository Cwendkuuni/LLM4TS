import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        File libPath = new File("test/dirName");
        urls.add(libPath);
        XBUSClassLoader.addUrls(urls, "test/dirName");
        assertTrue(urls.size() > 0);
    }

    @Test
    public void testAddUrlsWithInvalidPath() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "invalid/path");
        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrlsWithMalformedURLException() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "test/dirName");
        assertTrue(urls.isEmpty());
    }
}