package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[0], null);
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertSame(instance, XBUSClassLoader.mClassLoader);

        ClassLoader parent2 = new URLClassLoader(new URL[0], null);
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(parent2);
        assertSame(instance2, XBUSClassLoader.mClassLoader);

        assertEquals(parent, instance.getParent());

        assertEquals(parent2, instance2.getParent());
    }

    @Test
    public void testCreateClassLoader() throws MalformedURLException {
        ClassLoader parent = new URLClassLoader(new URL[0], null);
        XBUSClassLoader.createClassLoader(parent);

        assertSame(parent, XBUSClassLoader.mClassLoader.getParent());
        assertNull(XBUSClassLoader.createClassLoader(null));
    }

    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        String dirName = "testDir";
        File libPath = new File(dirName);
        File[] jars = {new File("file1.jar"), new File("file2.zip")};
        URL url1 = new URL("file://testDir/file1.jar");
        URL url2 = new URL("file://testDir/file2.zip");

        XBUSClassLoader.addUrls(urls, dirName);;
        for (URL url : urls) {
            assertTrue(url.equals(url1) || url.equals(url2));
        }
    }
}