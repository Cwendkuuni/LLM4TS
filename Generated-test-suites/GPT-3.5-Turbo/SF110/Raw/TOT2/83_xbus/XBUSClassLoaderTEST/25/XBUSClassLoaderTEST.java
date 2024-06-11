import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader instance = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(instance);
    }

    @Test
    public void testAddUrls() {
        // Edge case: directory with no jars or zips
        XBUSClassLoader loader = new XBUSClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        loader.addUrls(new Vector(), "nonexistent-directory");
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedURLException() {
        // Error scenario: malformed URL
        XBUSClassLoader loader = new XBUSClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        loader.addUrls(new Vector(), "malformed-url");
    }
}