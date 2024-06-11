import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/dir");
        assertEquals(0, urls.size());
        // Add more test cases based on different directories and file types
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    // Add more test methods to cover all methods in XBUSClassLoader

}