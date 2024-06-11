
import static org.junit.Assert.*;
import org.junit.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader1);
        
        // Edge case
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(null);
        assertNull(classLoader2);
    }

    @Test
    public void testAddUrls() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        
        // Typical use case
        Vector urls = new Vector();
        classLoader.addUrls(urls, "testDir");
        assertEquals(1, urls.size());
        
        // Error scenario
        urls.clear();
        classLoader.addUrls(urls, "nonExistentDir");
        assertEquals(0, urls.size());
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        
        // Typical use case
        XBUSClassLoader createdClassLoader1 = classLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(createdClassLoader1);
        
        // Edge case
        XBUSClassLoader createdClassLoader2 = classLoader.createClassLoader(null);
        assertNotNull(createdClassLoader2);
    }

    @Test
    public void testGetClassLoader() {
        // Typical use case
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        XBUSClassLoader classLoader2 = XBUSClassLoader.getClassLoader();

        assertSame(classLoader1, classLoader2);
    }
}

