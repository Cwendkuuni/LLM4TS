import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);

        // Edge case: Parent class loader is null
        classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Edge case: Constants.XBUS_HOME is null
        System.setProperty("XBUS_HOME", null);
        try {
            XBUSClassLoader.createClassLoader(getClass().getClassLoader());
            fail("Expected System.exit(1) with error message 'XBUS_HOME has not been set!'");
        } catch (SecurityException e) {
            // Exiting system is not allowed in JUnit tests, so SecurityException is expected
        }
        System.setProperty("XBUS_HOME", "some/valid/path");

        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        // Typical use case: Add valid JAR file URL
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "path/to/valid/jars");
        assertEquals(1, urls.size());
        // Additional checks can be performed to ensure the correct URL is added

        // Error scenario: Invalid directory name
        urls.clear();
        XBUSClassLoader.addUrls(urls, "invalid/directory");
        assertTrue(urls.isEmpty());

        // Edge case: directory contains no JAR files
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/no/jars");
        assertTrue(urls.isEmpty());

        // Edge case: directory contains non-JAR files
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/non/jar/files");
        assertTrue(urls.isEmpty());

        // Error scenario: Malformed URL
        urls.clear();
        XBUSClassLoader.addUrls(urls, "path/to/valid/jars/malformed");
        assertTrue(urls.isEmpty());
    }
}