import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    private XBUSClassLoader xbusClassLoader;

    @Before
    public void setUp() {
        xbusClassLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader testClassLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(testClassLoader);
        assertSame(testClassLoader, xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "testDir");
        assertFalse(urls.isEmpty());
        assertEquals(2, urls.size());
        assertEquals("testDir/test.jar", urls.get(0).getPath());
        assertEquals("testDir/test.zip", urls.get(1).getPath());
    }

    // Edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testCreateClassLoaderWithNullParent() {
        XBUSClassLoader.createClassLoader(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddUrlsWithNullVector() {
        XBUSClassLoader.addUrls(null, "testDir");
    }

    @Test(expected = NullPointerException.class)
    public void testAddUrlsWithNullDirectoryName() {
        XBUSClassLoader.addUrls(new Vector<>(), null);
    }

    @Test
    public void testAddUrlsWithEmptyDirectory() throws IOException {
        File emptyDir = File.createTempFile("emptyDir", "");
        emptyDir.delete();
        emptyDir.mkdir();

        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, emptyDir.getAbsolutePath());

        assertTrue(urls.isEmpty());

        emptyDir.delete();
    }
}

