package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import java.util.*;
import java.net.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new ClassLoader() {};
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        //Assert that instance is not null
        assertNotNull(instance);
        //Assert that instance is of type XBUSClassLoader
        assertTrue(instance instanceof XBUSClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new ClassLoader() {};
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        //Assert that classLoader is not null
        assertNotNull(classLoader);
        //Assert that classLoader is of type XBUSClassLoader
        assertTrue(classLoader instanceof XBUSClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "testDir";
        XBUSClassLoader.addUrls(urls, dirName);
        //Assert that urls is not empty
        assertFalse(urls.isEmpty());
        //Assert that urls contains valid URLs
        for (Object url : urls) {
            assertTrue(url instanceof URL);
        }
    }
}