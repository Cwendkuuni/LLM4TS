package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class XBUSClassLoaderTest {
    
    @Test
    public void testCreateClassLoader() throws MalformedURLException {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        
        assertEquals(null, classLoader.getParent());
        assertEquals(0, classLoader.getURLs().length);
    }
    
    @Test
    public void testGetInstance() throws MalformedURLException {
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(null);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(null);
        
        assertEquals(classLoader1, classLoader2);
        assertEquals(null, classLoader1.getParent());
        assertEquals(0, classLoader1.getURLs().length);
    }
    
    @Test
    public void testAddUrls() throws MalformedURLException {
        Vector<URL> urls = new Vector<>();
        String dirName = Constants.XBUS_HOME + "/lib";
        
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals(0, urls.size());
    }
}