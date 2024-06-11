package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(classLoader);
    }
    
    @Test
    public void testCreateClassLoaderWithNullParent() {
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
        assertNotNull(classLoader);
    }
    
    @Test
    public void testCreateClassLoaderWithNonNullParent() {
        XBUSClassLoader parentClassLoader = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parentClassLoader);
        assertNotNull(classLoader);
        assertEquals(parentClassLoader, classLoader.getParent());
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/dir";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
        
        urls = new Vector<>();
        dirName = "path/to/dir";
        new File(dirName).mkdirs();
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
        
        urls = new Vector<>();
        dirName = "path/to/dir";
        File file = new File(dirName + "/file.jar");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(1, urls.size());
    }
}