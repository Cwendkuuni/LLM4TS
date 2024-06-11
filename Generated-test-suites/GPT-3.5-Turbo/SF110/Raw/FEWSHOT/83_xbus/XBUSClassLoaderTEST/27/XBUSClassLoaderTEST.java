import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(null);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();

        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/dir";

        XBUSClassLoader.addUrls(urls, dirName);

        // Assert that the vector contains the correct number of URLs
        assertEquals(3, urls.size());

        // Assert that the URLs in the vector are valid
        try {
            for (URL url : urls) {
                assertNotNull(url);
                assertTrue(url.toString().startsWith("file:/"));
            }
        } catch (MalformedURLException e) {
            fail("MalformedURLException thrown");
        }
    }
}