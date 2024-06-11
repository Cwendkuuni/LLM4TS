
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test that getInstance returns a non-null object
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Test that createClassLoader returns a non-null object
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        // Test that addUrls adds URLs to the vector
        Vector urls = new Vector();
        String dirName = "test";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }
}
