
import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "/path/to/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

}
