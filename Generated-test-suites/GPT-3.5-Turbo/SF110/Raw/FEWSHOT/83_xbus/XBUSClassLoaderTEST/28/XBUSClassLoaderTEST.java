package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;
import java.net.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Test case for ClassLoader parent not null
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);

        // Test case for ClassLoader parent null
        parent = null;
        instance = XBUSClassLoader.getInstance(parent);
        assertNotNull(instance);
    }

    @Test
    public void testCreateClassLoader() {
        // Test case for XBUSClassLoader.mClassLoader not null
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader.createClassLoader(parent);
        assertNotNull(XBUSClassLoader.mClassLoader);

        // Test case for XBUSClassLoader.mClassLoader null
        XBUSClassLoader.mClassLoader = null;
        parent = null;
        XBUSClassLoader.createClassLoader(parent);
        assertNotNull(XBUSClassLoader.mClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();

        // Test case for jars array null
        String dirName = "non_existing_dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());

        // Test case for jars array not null
        urls = new Vector<>();
        dirName = "test_lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
    }
}