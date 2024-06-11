import org.junit.Test;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import java.io.File;
import java.net.MalformedURLException;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }
    
    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "dirName");
        assertEquals(0, urls.size());
        
        urls.clear();
        XBUSClassLoader.addUrls(urls, "lib");
        assertEquals(0, urls.size());
        
        urls.clear();
        XBUSClassLoader.addUrls(urls, "test/lib");
        assertEquals(0, urls.size());
        
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/libs");
        assertEquals(0, urls.size());
        
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/dir");
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }
    
    @Test
    public void testClassLoaderInitialization() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        
        XBUSClassLoader classLoader2 = XBUSClassLoader.getClassLoader();
        assertEquals(classLoader, classLoader2);
    }
}