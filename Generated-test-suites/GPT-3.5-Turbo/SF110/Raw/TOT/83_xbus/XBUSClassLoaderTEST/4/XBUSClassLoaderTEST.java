import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        
        // Test that the returned instance is not null
        assertNotNull(instance);
        
        // Test that the returned instance is of type XBUSClassLoader
        assertTrue(instance instanceof XBUSClassLoader);
        
        // Test that the returned instance has the correct parent class loader
        assertEquals(parent, instance.getParent());
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        
        // Test that the created class loader is not null
        assertNotNull(classLoader);
        
        // Test that the created class loader is of type XBUSClassLoader
        assertTrue(classLoader instanceof XBUSClassLoader);
        
        // Test that the created class loader has the correct parent class loader
        assertEquals(parent, classLoader.getParent());
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        
        // Test that the URLs vector is not empty
        assertFalse(urls.isEmpty());
        
        // Test that all URLs in the vector are valid URLs
        for (URL url : urls) {
            assertNotNull(url);
        }
    }
    
    @Test
    public void testGetClassLoaderSingleton() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(parent);
        
        // Test that the two instances are the same
        assertSame(instance1, instance2);
    }
}