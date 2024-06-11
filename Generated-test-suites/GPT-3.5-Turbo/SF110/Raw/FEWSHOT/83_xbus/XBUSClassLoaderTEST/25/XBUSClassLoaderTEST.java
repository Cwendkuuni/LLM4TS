package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;
import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;

public class XBUSClassLoaderTest {
    private XBUSClassLoader classLoader;

    @Before
    public void setUp() {
        classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
    }

    @Test
    public void testGetInstance() {
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader loader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(loader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
        assertNotEmpty(urls);
    }

    @Test
    public void testAddUrlsWithInvalidDirectory() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "invalid_directory");
        assertEmpty(urls);
    }

    @Test
    public void testAddUrlsWithNonJarOrZipFile() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/dir_with_non_jar_zip_file");
        assertEmpty(urls);
    }

    @Test
    public void testAddUrlsWithNullDirectory() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, null);
        assertEmpty(urls);
    }

    @Test
    public void testAddUrlsWithEmptyDirectory() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "");
        assertEmpty(urls);
    }

    @Test
    public void testAddUrlsWithIOException() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib_with_io_exception");
        assertEmpty(urls);
    }

    private void assertEmpty(Vector<URL> urls) {
        assertEquals(0, urls.size());
    }

    private void assertNotEmpty(Vector<URL> urls) {
        assertNotNull(urls);
        assertNotEquals(0, urls.size());
    }
}