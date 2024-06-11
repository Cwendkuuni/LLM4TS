import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Test get instance with parent
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull("Instance should not be null", instance);
        assertSame("Parent should be the same", parent, instance.getParent());
    }
    
    @Test
    public void testCreateClassLoader() {
        // Test create class loader without parent
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull("Class loader should not be null", classLoader);
        assertNull("Parent should be null", classLoader.getParent());
        
        // Test create class loader with parent
        ClassLoader parent = new ClassLoader();
        classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull("Class loader should not be null", classLoader);
        assertSame("Parent should be the same", parent, classLoader.getParent());
    }
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "test-dir";
        
        // Test addUrls with null directory
        XBUSClassLoader.addUrls(urls, null);
        assertTrue("URLs should be empty", urls.isEmpty());
        
        // Test addUrls with empty directory
        XBUSClassLoader.addUrls(urls, "");
        assertTrue("URLs should be empty", urls.isEmpty());
        
        // Test addUrls with non-existent directory
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue("URLs should be empty", urls.isEmpty());
        
        // Test addUrls with directory containing non-jar/zip files
        urls.clear();
        XBUSClassLoader.addUrls(urls, "test-dir-2");
        assertTrue("URLs should be empty", urls.isEmpty());
        
        // Test addUrls with directory containing jar/zip files
        urls.clear();
        XBUSClassLoader.addUrls(urls, "test-dir-3");
        assertFalse("URLs should not be empty", urls.isEmpty());
    }
}