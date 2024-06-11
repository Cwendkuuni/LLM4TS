
package net.sf.xbus.base.core.reflection;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    private XBUSClassLoader classLoader;

    @Before
    public void setUp() {
        // Create an instance of XBUSClassLoader with a dummy parent class loader
        classLoader = XBUSClassLoader.getInstance(new ClassLoader() {});
    }

    @Test
    public void testCreateClassLoader() {
        // Test if the createClassLoader method returns a non-null instance of XBUSClassLoader
        assertNotNull(XBUSClassLoader.createClassLoader(new ClassLoader() {}));
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "testDir";

        // Test if addUrls method adds URLs to the vector correctly
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
        assertEquals(0, urls.size());

        // Test if addUrls method correctly adds URLs to the vector for a directory with jar and zip files
        XBUSClassLoader.addUrls(urls, "testResources");
        assertEquals(2, urls.size());
        assertEquals("testResources/test.jar", urls.get(0).getPath());
        assertEquals("testResources/test.zip", urls.get(1).getPath());
    }
}
