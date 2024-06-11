
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

public class XBUSClassLoaderTest {

    private static XBUSClassLoader xbusClassLoader;
    private static ClassLoader parentClassLoader;

    @BeforeClass
    public static void setup() {
        parentClassLoader = ClassLoader.getSystemClassLoader();
        xbusClassLoader = XBUSClassLoader.getInstance(parentClassLoader);
    }

    @Test
    public void testGetInstance() {
        assertNotNull(XBUSClassLoader.getInstance(parentClassLoader));
    }

    @Test
    public void testCreateClassLoader() {
        assertNotNull(XBUSClassLoader.createClassLoader(parentClassLoader));
    }

    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();

        XBUSClassLoader.addUrls(urls, dirName);

        for (int i = 0; jars != null && i < jars.length; ++i) {
            if (jars[i].isFile()) {
                assertNotNull(urls.get(i));
            }
        }
    }
}
