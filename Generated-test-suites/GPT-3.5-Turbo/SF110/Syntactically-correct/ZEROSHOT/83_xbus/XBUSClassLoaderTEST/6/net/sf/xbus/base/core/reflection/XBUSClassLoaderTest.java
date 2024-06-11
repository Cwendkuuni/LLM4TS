import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    private static final String XBUS_HOME = "/path/to/xbus/home";
    private static final URL URL_1 = new URL("file:/path/to/file1.jar");
    private static final URL URL_2 = new URL("file:/path/to/file2.jar");

    private XBUSClassLoader classLoader;

    @Before
    public void setUp() {
        classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader loader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(loader);
    }

    @Test
    public void testGetUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib");
        XBUSClassLoader.addUrls(urls, XBUS_HOME + "/lib/runtime");
        XBUSClassLoader.addUrls(urls, XBUS_HOME + "/plugin/lib");
        XBUSClassLoader.addUrls(urls, XBUS_HOME + "/test/lib");
        
        assertEquals("", urls.size(), 4);
        assertEquals("", urls.elementAt(0), URL_1);
        assertEquals("", urls.elementAt(1), URL_2);
    }
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader loader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(loader);
    }
}