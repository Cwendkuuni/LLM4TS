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
    public void testAddUrls() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "invalidDirectory");
        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrlsWithMalformedURL() {
        Vector urls = new Vector();
        try {
            XBUSClassLoader.addUrls(urls, "invalidDirectory");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected MalformedURLException
        }
    }
}