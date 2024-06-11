
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
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
        String dirName = "test/dirName";
        XBUSClassLoader.addUrls(urls, dirName);
        // Assert that the urls vector is not empty
        assertFalse(urls.isEmpty());
        // Assert that the urls vector contains the correct number of URLs
        assertEquals(0, urls.size());
    }

}
