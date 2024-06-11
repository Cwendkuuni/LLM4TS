
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URLClassLoader;
import java.net.URL;

public class XBUSClassLoaderTest {

    private XBUSClassLoader xbusClassLoader;
    private ClassLoader parentClassLoader;

    @Before
    public void setUp() {
        URL[] urlArray = {};
        parentClassLoader = ClassLoader.getSystemClassLoader();
        xbusClassLoader = XBUSClassLoader.getInstance(parentClassLoader);
    }

    @Test
    public void testGetInstance() {
        assertSame(xbusClassLoader, XBUSClassLoader.getInstance(parentClassLoader));
    }

    @Test
    public void testCreateClassLoader() {
        assertNotNull(XBUSClassLoader.createClassLoader(parentClassLoader));
    }

    @Test
    public void testAddUrls() throws Exception {
        URL url1 = new File("test1.jar").toURI().toURL();
        URL url2 = new File("test2.zip").toURI().toURL();
        
        Vector<URL> urls = new Vector<>();
        urls.add(url1);
        urls.add(url2);
        
        XBUSClassLoader.addUrls(urls, "testDir");
        
        assertArrayEquals(new URL[]{url1, url2}, urls.toArray());
    }

    @Test
    public void testAddUrlsWhenDirectoryDoesNotExist() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "nonExistentDir");
        assertTrue(urls.isEmpty());
    }
}

