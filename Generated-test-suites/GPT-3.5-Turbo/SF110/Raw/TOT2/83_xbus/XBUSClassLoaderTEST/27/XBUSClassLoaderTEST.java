import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.io.File;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        XBUSClassLoader classLoader = new XBUSClassLoader(null, getClass().getClassLoader());
        Vector urls = new Vector();
        File file = new File("test.txt");
        urls.add(file.toURL());
        try {
            classLoader.addUrls(urls, "testDir");
        } catch (MalformedURLException e) {
            fail("MalformedURLException should not be thrown");
        }
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(getClass().getClassLoader());
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertEquals(classLoader1, classLoader2);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}