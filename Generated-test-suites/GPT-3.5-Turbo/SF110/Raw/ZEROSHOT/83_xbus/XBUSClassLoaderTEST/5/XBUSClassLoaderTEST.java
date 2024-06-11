package net.sf.xbus.base.core.reflection;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;

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
        String dirName = "test";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertFalse(urls.isEmpty());
        for (URL url : urls) {
            assertNotNull(url);
        }
    }
}