
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Test not null instance when parent class loader is not null
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
        
        // Test not null instance when parent class loader is null
        classLoader = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoader);
    }

    @Test
    public void testCreateClassLoader() {
        // Test not null classLoader instance
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertNotNull(classLoader);
        
        // Test classLoader instance is singleton
        XBUSClassLoader classLoader1 = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        XBUSClassLoader classLoader2 = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
        assertSame(classLoader1, classLoader2);
        
        // Test System exit when Constants.XBUS_HOME is null
        Constants.XBUS_HOME = null;
        try {
            XBUSClassLoader.createClassLoader(getClass().getClassLoader());
            fail("Expected System.exit(1)");
        } catch (SecurityException se) {
            assertNull(Constants.XBUS_HOME);
        }
    }

    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "test/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        
        // Test urls are added correctly when files have .jar extension
        assertTrue(urls.contains(createURL("test/dir/file1.jar")));
        assertTrue(urls.contains(createURL("test/dir/file2.jar")));
        
        // Test urls are not added when files don't have .jar extension
        assertFalse(urls.contains(createURL("test/dir/file1.txt")));
        assertFalse(urls.contains(createURL("test/dir/file2.txt")));
        
        // Test urls are not added when files don't have .zip extension
        assertFalse(urls.contains(createURL("test/dir/file1.zip")));
        assertFalse(urls.contains(createURL("test/dir/file2.zip")));
    }
    
    private URL createURL(String path) {
        try {
            return new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
