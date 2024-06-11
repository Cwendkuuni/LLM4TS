import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "test";
        XBUSClassLoader.addUrls(urls, dirName);
        assertTrue(urls.isEmpty());
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedURL() {
        Vector<URL> urls = new Vector<>();
        String dirName = "invalid";
        XBUSClassLoader.addUrls(urls, dirName);
    }
}