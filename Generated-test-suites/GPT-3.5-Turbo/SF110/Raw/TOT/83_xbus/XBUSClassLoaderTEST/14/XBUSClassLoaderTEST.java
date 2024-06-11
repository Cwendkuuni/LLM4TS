import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);

        // Edge case: parent class loader is null
        classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);

        // Edge case: parent class loader is null
        classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);

        // Error scenario: XBUS_HOME is not set
        // Expert 3's test case captures this scenario
    }

    @Test
    public void testAddUrls() {
        // Typical use case
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "/path/to/directory");
        assertFalse(urls.isEmpty());

        // Edge case: directory is empty
        urls.clear();
        XBUSClassLoader.addUrls(urls, "/empty/directory");
        assertTrue(urls.isEmpty());

        // Error scenario: directory does not exist
        // Expert 2's test case captures this scenario
    }
}