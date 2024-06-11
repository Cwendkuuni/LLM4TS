package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
        
        // Edge case - passing null as parent class loader
        XBUSClassLoader classLoaderNullParent = XBUSClassLoader.getInstance(null);
        assertNotNull(classLoaderNullParent);
        
    }

    @Test
    public void testAddUrls() {
        // Typical use case
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        Vector<URL> urls = new Vector<>();
        XBUSClassLoader.addUrls(urls, "/some/path/to/jar/files");
        assertFalse(urls.isEmpty());

        // Edge case - passing an invalid directory that does not contain any jar files
        Vector<URL> urlsEmpty = new Vector<>();
        XBUSClassLoader.addUrls(urlsEmpty, "/invalid/path");
        assertTrue(urlsEmpty.isEmpty());
        
        // Error scenario - passing a directory with invalid files (e.g., not jar or zip)
        Vector<URL> urlsInvalidFiles = new Vector<>();
        XBUSClassLoader.addUrls(urlsInvalidFiles, "/some/path/to/invalid/files");
        assertTrue(urlsInvalidFiles.isEmpty());
    }

    // Add test cases for other public and private methods in XBUSClassLoader

}