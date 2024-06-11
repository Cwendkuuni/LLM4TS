package net.sf.xbus.base.core.reflection;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;
import java.net.*;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    private XBUSClassLoader xbusClassLoader;
    private URLClassLoader parentClassLoader;

    @Before
    public void setUp() {
        parentClassLoader = new URLClassLoader(new URL[]{});
        xbusClassLoader = XBUSClassLoader.getInstance(parentClassLoader);
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parentClassLoader);
        assertNotNull(instance);
        assertEquals(xbusClassLoader, instance);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
        assertNotNull(classLoader);
        assertEquals(xbusClassLoader, classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "dirName");

        assertEquals(0, urls.size());
    }

    @Test
    public void testAddUrlsWithValidDirName() {
        Vector<URL> urls = new Vector<>();
        String dirName = "src/test/resources";

        XBUSClassLoader.addUrls(urls, dirName);

        assertEquals(1, urls.size());
    }

    @Test
    public void testAddUrlsWithInvalidDirName() {
        Vector<URL> urls = new Vector<>();
        String dirName = "invalidDirName";

        XBUSClassLoader.addUrls(urls, dirName);

        assertEquals(0, urls.size());
    }
}