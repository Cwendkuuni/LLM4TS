
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "non-existent-directory");
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testAddUrlsWithMalformedURL() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/resources/malformed.jar");
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testAddUrlsWithValidURL() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/resources/test.jar");
        assertEquals(1, urls.size());
    }
}
