package net.sf.xbus.base.core.reflection;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
        assertFalse(urls.isEmpty());
    }
}