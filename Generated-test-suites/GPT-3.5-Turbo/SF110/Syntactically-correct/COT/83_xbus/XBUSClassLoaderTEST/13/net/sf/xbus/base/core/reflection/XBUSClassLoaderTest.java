
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(Thread.currentThread().getContextClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(Thread.currentThread().getContextClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "test/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }
    
    @Test
    public void testAddUrlsWithInvalidDir() {
        Vector<URL> urls = new Vector<>();
        String dirName = "invalid/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }
}
