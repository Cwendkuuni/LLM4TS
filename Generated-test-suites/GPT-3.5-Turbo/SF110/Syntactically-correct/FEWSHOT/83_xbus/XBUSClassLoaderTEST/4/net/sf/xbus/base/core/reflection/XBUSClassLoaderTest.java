package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);

        assertNotNull(classLoader);
        assertNotNull(classLoader.getParent());
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);

        assertNotNull(classLoader);
        assertNotNull(classLoader.getParent());
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "testDir";

        File libPath = new File(dirName);
        libPath.mkdir();

        File jarFile = new File(dirName + "/test.jar");
        try {
            jarFile.createNewFile();
        } catch (Exception e) {
            fail("Failed to create test.jar");
        }

        XBUSClassLoader.addUrls(urls, dirName);

        assertEquals(1, urls.size());
        assertEquals(jarFile.toURI().toURL(), urls.get(0));

        // Cleanup
        jarFile.delete();
        libPath.delete();
    }
}