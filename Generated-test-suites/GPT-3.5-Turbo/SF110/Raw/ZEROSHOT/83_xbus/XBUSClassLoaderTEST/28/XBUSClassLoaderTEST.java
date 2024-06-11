package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Create a parent class loader
        ClassLoader parentClassLoader = getClass().getClassLoader();
        
        // Get the instance of XBUSClassLoader
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parentClassLoader);
        
        // Assert that the returned instance is not null
        assertNotNull(xbusClassLoader);
        
        // Assert that the returned instance is an instance of XBUSClassLoader
        assertTrue(xbusClassLoader instanceof XBUSClassLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        // Create a parent class loader
        ClassLoader parentClassLoader = getClass().getClassLoader();
        
        // Call the createClassLoader method
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
        
        // Assert that the returned instance is not null
        assertNotNull(xbusClassLoader);
        
        // Assert that the returned instance is an instance of XBUSClassLoader
        assertTrue(xbusClassLoader instanceof XBUSClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        // Create a new Vector to store the URLs
        Vector<URL> urls = new Vector<>();
        
        // Call the addUrls method with a valid directory name
        XBUSClassLoader.addUrls(urls, "testDirectory");
        
        // Assert that the size of the vector is not 0
        assertNotEquals(0, urls.size());
        
        // Assert that the elements in the vector are all valid URLs
        for (URL url : urls) {
            assertNotNull(url);
        }
    }

}