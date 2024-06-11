package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;


public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(getClass().getClassLoader());
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(getClass().getClassLoader());
        
        assertSame(classLoader1, classLoader2);
    }
    
    @Test
    public void testCreateClassLoader_withValidXBUSHome() {
        // Set valid XBUS_HOME
        Constants.XBUS_HOME = "path/to/xbus/home";
        
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        
        assertNotNull(classLoader);
    }
    
    @Test
    public void testCreateClassLoader_withNullXBUSHome() {
        // Set null XBUS_HOME
        Constants.XBUS_HOME = null;
        
        // Ensure Trace.error is called and system exits
        try {
            XBUSClassLoader.createClassLoader(getClass().getClassLoader());
            fail("Expected System.exit(1) to be called");
        } catch (Exception e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAddUrls_withValidDir() throws URISyntaxException {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/directory";
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertFalse(urls.isEmpty());
    }
    
    @Test
    public void testAddUrls_withInvalidDir() throws URISyntaxException {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/invalid/directory";
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertTrue(urls.isEmpty());
    }
    
}