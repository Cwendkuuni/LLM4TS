import static org.junit.Assert.*;
import org.junit.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test case for getInstance() method
        ClassLoader parent = getClass().getClassLoader();
        
        XBUSClassLoader xBusClassLoader = XBUSClassLoader.getInstance(parent);
        
        assertNotNull("Instance should not be null", xBusClassLoader);
        assertEquals("Parent class loader should be same", parent, xBusClassLoader.getParent());
    }
    
    @Test
    public void testCreateClassLoader() {
        // Test case for createClassLoader() method
        ClassLoader parent = getClass().getClassLoader();
        
        XBUSClassLoader xBusClassLoader = XBUSClassLoader.createClassLoader(parent);
        
        assertNotNull("Instance should not be null", xBusClassLoader);
        assertEquals("Parent class loader should be same", parent, xBusClassLoader.getParent());
    }
    
    @Test
    public void testAddUrls() {
        // Test case for addUrls() method
        
        String dirName = "/test/dir";
        Vector urls = new Vector();
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals("Number of URLs should be 0", 0, urls.size());
        
        dirName = "/test/lib";
        urls.clear();
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals("Number of URLs should be greater than 0", true, urls.size() > 0);
    }
}