import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() throws MalformedURLException {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
        assertNotNull(instance);
    }
    
    @Test
    public void testCreateClassLoader() throws MalformedURLException {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(this.getClass().getClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/dir");

        assertTrue(urls.isEmpty());
    }
    
    @Test
    public void testAddUrlsWithJar() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/jars");

        assertEquals(1, urls.size());
        assertEquals(new URL("file:/test/jars/test.jar"), urls.get(0));
    }
    
    @Test
    public void testAddUrlsWithZip() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/zip");

        assertEquals(1, urls.size());
        assertEquals(new URL("file:/test/zip/test.zip"), urls.get(0));
    }
    
    @Test
    public void testAddUrlsWithOtherFile() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "test/other");

        assertTrue(urls.isEmpty());
    }
    
}