import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Test the getInstance method to ensure it returns XBUSClassLoader instance
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Test the createClassLoader method to ensure it creates XBUSClassLoader instance
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        // Test the addUrls method to ensure it correctly adds URLs to the vector
        Vector urls = new Vector();
        String dirName = "testDir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.size() > 0);
    }
}