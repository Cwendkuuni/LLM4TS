import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        File dir = new File("testDir");
        dir.mkdir();
        try {
            XBUSClassLoader.addUrls(urls, "testDir");
            assertEquals(0, urls.size());
        } catch (MalformedURLException e) {
            fail("MalformedURLException should not be thrown");
        } finally {
            dir.delete();
        }
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(null);
        assertEquals(classLoader, classLoader2);
    }
}