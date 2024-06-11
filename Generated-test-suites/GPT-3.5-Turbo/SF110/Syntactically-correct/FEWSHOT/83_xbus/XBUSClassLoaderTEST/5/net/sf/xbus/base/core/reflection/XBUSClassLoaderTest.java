
package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = null;
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = null;
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "testDir";
        
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
        
        dirName = "testDir1";
        File libPath = new File(dirName);
        libPath.mkdir();
        
        File file1 = new File(dirName + "/file1.jar");
        try {
            file1.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file2 = new File(dirName + "/file2.zip");
        try {
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file3 = new File(dirName + "/file3.txt");
        try {
            file3.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(2, urls.size());
        
        file1.delete();
        file2.delete();
        file3.delete();
        libPath.delete();
    }
}
