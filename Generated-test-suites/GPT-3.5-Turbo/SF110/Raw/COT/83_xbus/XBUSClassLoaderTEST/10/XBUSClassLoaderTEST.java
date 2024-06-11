
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }
}
