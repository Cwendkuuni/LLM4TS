
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test case for a valid ClassLoader parent
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
        
        // Test case for null ClassLoader parent
        instance = XBUSClassLoader.getInstance(null);
        assertNotNull(instance);

        // Add more test cases for different scenarios if necessary
    }

    @Test
    public void testCreateClassLoader() {
        // Test case for a valid ClassLoader parent
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
        
        // Test case for null ClassLoader parent
        classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);

        // Add more test cases for different scenarios if necessary
    }

    @Test
    public void testAddUrls() {
        // Test case for a valid directory name
        Vector urls = new Vector();
        String dirName = "testDir";
        XBUSClassLoader.addUrls(urls, dirName);
        // Check if the URLs were added correctly
        
        // Test case for a directory that does not exist
        urls = new Vector();
        dirName = "invalidDir";
        XBUSClassLoader.addUrls(urls, dirName);
        // Check if no URLs were added
        
        // Add more test cases for different scenarios if necessary
    }

}
