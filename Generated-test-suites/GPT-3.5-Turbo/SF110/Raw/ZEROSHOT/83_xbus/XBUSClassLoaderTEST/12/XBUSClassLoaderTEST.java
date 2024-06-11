import org.junit.*;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "testDir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        dirName = "testDirWithJars";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
        assertTrue(urls.contains(new File(dirName + "/test.jar").toURL()));
        assertTrue(urls.contains(new File(dirName + "/test.zip").toURL()));
    }
}