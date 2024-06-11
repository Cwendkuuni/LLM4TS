import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(instance);
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "lib";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertFalse(urls.isEmpty());
        for (URL url : urls) {
            assertNotNull(url);
        }
    }
}