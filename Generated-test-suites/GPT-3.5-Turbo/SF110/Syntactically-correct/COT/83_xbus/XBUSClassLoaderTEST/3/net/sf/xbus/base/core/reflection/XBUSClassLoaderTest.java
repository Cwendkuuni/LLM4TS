
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

    private XBUSClassLoader classLoader;

    @Before
    public void setUp() throws MalformedURLException {
        URL[] urls = new URL[0];
        classLoader = new XBUSClassLoader(urls, getClass().getClassLoader());
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<String> urls = new Vector<>();
        urls.add("example-url");
        XBUSClassLoader.addUrls(urls, "example-directory");
        assertEquals(1, urls.size());
    }
}
