
import org.junit.Test;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        Assert.assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/dummy/path");
        Assert.assertTrue(urls.isEmpty());
    }

    @Test(expected = MalformedURLException.class)
    public void testAddUrlsWithMalformedURL() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "dummy/malformed/url");
    }

    @Test
    public void testAddUrlsWithValidURLs() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/dummy/path");
        Assert.assertEquals(2, urls.size());
    }
}

