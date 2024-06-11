package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;

public class XBUSClassLoaderTest {

    @Test
    public void testCreateClassLoader() {
        assertNull(XBUSClassLoader.mClassLoader);
        assertNull(XBUSClassLoader.createClassLoader(null));
        
        // Set XBUS_HOME for testing
        Constants.XBUS_HOME = "/path/to/xbus/home";
        assertNotNull(XBUSClassLoader.createClassLoader(null));
        
        // Test if XBUS_HOME is null
        Constants.XBUS_HOME = null;
        XBUSClassLoader.createClassLoader(null);
        assertTrue(Trace.getLog().contains("XBUS_HOME has not been set!"));
        assertEquals(1, Trace.getErrorCount());
        System.exit(1);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        File libPath = new File(dirName);
        File[] jars = libPath.listFiles();
        for (int i = 0; jars != null && i < jars.length; i++) {
            if (jars[i].isFile()) {
                if (!jars[i].getName().endsWith("jar")) {
                    if (!jars[i].getName().endsWith("zip")) {
                        continue;
                    }
                }
                try {
                    urls.add(jars[i].toURL());
                } catch (MalformedURLException e) {
                    Trace.error(e);
                    System.exit(1);
                }
            }
        }
        // Verify that urls are added correctly
        assertEquals(urls.size(), XBUSClassLoader.addUrls(urls, dirName));
    }
    
    @Test
    public void testGetInstance() {
        assertNull(XBUSClassLoader.mClassLoader);
        assertNotNull(XBUSClassLoader.getInstance(null));
    }
    
}