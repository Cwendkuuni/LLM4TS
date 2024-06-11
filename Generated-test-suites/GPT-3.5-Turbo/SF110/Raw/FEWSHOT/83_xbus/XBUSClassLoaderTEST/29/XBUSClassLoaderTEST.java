package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    private XBUSClassLoader classLoader;

    @Before
    public void setup() {
        URL[] urlArray = null;
        ClassLoader parent = null;
        classLoader = new XBUSClassLoader(urlArray, parent);
    }

    @Test
    public void testGetInstance() {
        ClassLoader parent = null;
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = null;
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        dirName = "test";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        dirName = "path/to/libs";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        dirName = "path/to/jars";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        File file1 = new File("path/to/libs/lib1.jar");
        File file2 = new File("path/to/libs/lib2.jar");
        File file3 = new File("path/to/libs/lib3.txt");
        File file4 = new File("path/to/libs/lib4.zip");

        File[] jars = {file1, file2, file3, file4};
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
        assertArrayEquals(new URL[]{file1.toURL(), file2.toURL()}, urls.toArray());
    }
}