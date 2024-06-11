
package net.sf.xbus.base.core.reflection;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    private XBUSClassLoader classLoader;

    @Before
    public void setup() {
        classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance1 = XBUSClassLoader.getInstance(getClass().getClassLoader());
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(getClass().getClassLoader());

        assertEquals(instance1, instance2);
    }

    @Test
    public void testCreateClassLoader() throws NoSuchFieldException, IllegalAccessException {
        ClassLoader parent = getClass().getClassLoader();

        Field mClassLoaderField = XBUSClassLoader.class.getDeclaredField("mClassLoader");
        mClassLoaderField.setAccessible(true);
        mClassLoaderField.set(null, null);

        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);

        assertNotNull(mClassLoaderField.get(null));
        assertEquals(classLoader, mClassLoaderField.get(null));
    }

    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();

        XBUSClassLoader.addUrls(urls, "test/dir");

        assertEquals(new URL("file:test/dir/file.jar"), urls.get(0));
        assertEquals(new URL("file:test/dir/file.zip"), urls.get(1));
    }

    @Test
    public void testAddUrlsWithNonJarAndZipFiles() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();

        XBUSClassLoader.addUrls(urls, "test/dir");

        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithEmptyDirectory() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();

        XBUSClassLoader.addUrls(urls, "test/empty");

        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithNullDirectory() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();

        XBUSClassLoader.addUrls(urls, null);

        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithNullFileArray() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        File libPath = new File("test/null");

        XBUSClassLoader.addUrls(urls, libPath.getPath());

        assertTrue(urls.isEmpty());
    }

    @Test
    public void testAddUrlsWithNullFile() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        File[] jars = null;

        XBUSClassLoader.addUrls(urls, "test/null");

        assertTrue(urls.isEmpty());
    }

    @Test
    public void testConstructor() {
        URL[] urlArray = new URL[0];
        ClassLoader parent = getClass().getClassLoader();

        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, parent);

        assertSame(urlArray, classLoader.getURLs());
        assertSame(parent, classLoader.getParent());
    }

    @Test
    public void testConstructorWithNullURLArray() {
        URL[] urlArray = null;
        ClassLoader parent = getClass().getClassLoader();

        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, parent);

        assertNotNull(classLoader.getURLs());
        assertEquals(0, classLoader.getURLs().length);
        assertSame(parent, classLoader.getParent());
    }

    @Test
    public void testConstructorWithNullParent() {
        URL[] urlArray = new URL[0];
        ClassLoader parent = null;

        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, parent);

        assertSame(urlArray, classLoader.getURLs());
        assertNull(classLoader.getParent());
    }

    @Test
    public void testClassLock() throws NoSuchFieldException, IllegalAccessException {
        Field classLockField = XBUSClassLoader.class.getDeclaredField("classLock");
        classLockField.setAccessible(true);
        int modifiers = classLockField.getModifiers();

        assertTrue(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        assertEquals(XBUSClassLoader.class, classLockField.getType());
    }
}
