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
        assertEquals(parent, instance.getParent());
        assertEquals(XBUSClassLoader.class, instance.getClass());
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        assertEquals(parent, classLoader.getParent());
        assertEquals(XBUSClassLoader.class, classLoader.getClass());
    }
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "/path/to/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
        
        dirName = getClass().getResource("/test-dir").getPath();
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
        assertEquals(true, urls.contains(createUrl("/test-dir/jar1.jar")));
        assertEquals(true, urls.contains(createUrl("/test-dir/jar2.zip")));
    }
    
    @Test
    public void testAddUrlsWithInvalidDir() {
        Vector urls = new Vector();
        String dirName = "/invalid/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
    }
    
    private URL createUrl(String path) {
        try {
            return new URL("file://" + path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}