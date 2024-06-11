
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertNotNull(instance);
        
        XBUSClassLoader instance2 = XBUSClassLoader.getInstance(getClass().getClassLoader());
        assertEquals(instance, instance2);
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector<URL> urls = new Vector<>();
        String dirName = "path/to/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
        
        dirName = "path/to/directoryWithManyJars";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(3, urls.size());
    }
}
