
package net.sf.xbus.base.core.reflection;

import org.junit.Assert;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(xbusClassLoader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        Assert.assertNotNull(xbusClassLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<String> urls = new Vector<>();
        String dirName = "test/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        Assert.assertTrue(urls.size() > 0);
    }
}
