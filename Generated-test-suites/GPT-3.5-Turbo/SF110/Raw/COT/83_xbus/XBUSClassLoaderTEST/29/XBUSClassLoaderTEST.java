import org.junit.*;
import java.net.*;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/directory";
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        // Check that urls were added correctly
        Assert.assertEquals(expectedSize, urls.size());
        // Check that each URL is valid
        for (URL url : urls) {
            Assert.assertNotNull(url);
            Assert.assertTrue(url.getProtocol().equals("file"));
        }
    }
}