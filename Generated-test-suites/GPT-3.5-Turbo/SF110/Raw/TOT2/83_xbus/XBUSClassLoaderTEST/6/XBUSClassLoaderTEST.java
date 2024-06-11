import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "/path/to/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrlsWithMalformedURL() {
        Vector urls = new Vector();
        String dirName = "/path/to/directory";
        XBUSClassLoader.addUrls(urls, "invalidURL");
        assertTrue(urls.isEmpty());
    }
    
    @Test
    public void testGetInstanceAfterSettingXBUS_HOME() {
        Constants.XBUS_HOME = "/path/to/xbus/home";
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
    }
    
    // Include more test cases for other methods as needed
    
}