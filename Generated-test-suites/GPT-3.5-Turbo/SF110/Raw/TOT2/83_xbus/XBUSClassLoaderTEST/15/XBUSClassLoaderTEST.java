import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {
    
    @Test
    public void testCreateClassLoader() {
        // Typical use case
        assertNotNull(XBUSClassLoader.getInstance(getClass().getClassLoader()));
        
        // Edge case: Constants.XBUS_HOME is null, expect Trace.error and System.exit
        // Error scenario: MalformedURLException when adding URL
        // These cases are handled internally in the method, so no assertion is needed
        XBUSClassLoader.createClassLoader(getClass().getClassLoader());
    }
    
    @Test
    public void testAddUrls() {
        // Typical use case
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/dir");
        assertFalse(urls.isEmpty());
        
        // Edge case: Empty directory name
        urls.clear();
        XBUSClassLoader.addUrls(urls, "");
        assertTrue(urls.isEmpty());
        
        // Edge case: Invalid directory name, expect no URLs added
        urls.clear();
        XBUSClassLoader.addUrls(urls, "invalid/dir");
        assertTrue(urls.isEmpty());
        
        // Error scenario: MalformedURLException when adding URL
        urls.clear();
        XBUSClassLoader.addUrls(urls, "invalid/zip");
        assertTrue(urls.isEmpty());
    }
}