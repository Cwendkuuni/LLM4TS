import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "testDir";
        XBUSClassLoader.addUrls(urls, dirName);
        // Add assertion for the expected behavior
    }

    // Add test cases for other public methods in XBUSClassLoader

    @Test
    public void testInvalidDirectory() {
        Vector urls = new Vector();
        String dirName = "invalidDir";
        XBUSClassLoader.addUrls(urls, dirName);
        // Add assertion for handling of exception due to invalid directory
    }

    @Test
    public void testMissingXBUSHome() {
        Constants.XBUS_HOME = null;
        XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        // Add assertion for correct handling of missing XBUS_HOME
    }

    // Add more test cases for edge cases and exception handling scenarios

}