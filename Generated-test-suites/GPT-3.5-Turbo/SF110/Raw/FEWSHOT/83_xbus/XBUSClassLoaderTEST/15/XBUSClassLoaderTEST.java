
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        ClassLoader classLoader = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(classLoader);
        
        assertNotNull(xbusClassLoader);
        assertEquals(classLoader, xbusClassLoader.getParent());
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/dir";
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        // assert statements
        assertEquals(0, urls.size());
    }
}
