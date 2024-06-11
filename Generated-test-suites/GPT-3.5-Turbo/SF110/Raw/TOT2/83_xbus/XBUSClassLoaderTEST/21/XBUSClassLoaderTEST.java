import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Edge case: Constants.XBUS_HOME is null
        Constants.XBUS_HOME = null;
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNull(classLoader);

        // Typical use case
        Constants.XBUS_HOME = "XBUS_HOME";
        classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        // Edge case: dirName does not exist
        XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], getClass().getClassLoader());
        assertThrows(MalformedURLException.class, () -> classLoader.addUrls(new Vector(), "invalidDir"));

        // Typical use case
        Vector urls = new Vector();
        classLoader.addUrls(urls, "validDir");
        assertFalse(urls.isEmpty());
    }
}