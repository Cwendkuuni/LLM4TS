package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        
        try {
            XBUSClassLoader.addUrls(urls, dirName);
            assertTrue(urls.size() > 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddUrlsWithInvalidDirectory() {
        Vector<URL> urls = new Vector<>();
        String dirName = "invalid_directory";
        
        try {
            XBUSClassLoader.addUrls(urls, dirName);
            assertTrue(urls.size() == 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddUrlsWithNoFiles() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/empty_directory";
        
        try {
            XBUSClassLoader.addUrls(urls, dirName);
            assertTrue(urls.size() == 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}