import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test if getInstance returns mClassLoader instance
        XBUSClassLoader instance = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        // Test if createClassLoader initializes mClassLoader
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        // Test adding URLs to the Vector
        Vector urls = new Vector();
        String dirName = "test/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }
    
    @Test
    public void testCreateClassLoaderException() {
        // Test exception handling in createClassLoader
        try {
            XBUSClassLoader.createClassLoader(null);
            fail("Should throw exception for null parent ClassLoader");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
    
    @Test
    public void testAddUrlsException() {
        // Test exception handling in addUrls method
        Vector urls = new Vector();
        String dirName = "non_existent_dir_name";
        try {
            XBUSClassLoader.addUrls(urls, dirName);
            fail("Should throw exception for non-existent directory");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

}