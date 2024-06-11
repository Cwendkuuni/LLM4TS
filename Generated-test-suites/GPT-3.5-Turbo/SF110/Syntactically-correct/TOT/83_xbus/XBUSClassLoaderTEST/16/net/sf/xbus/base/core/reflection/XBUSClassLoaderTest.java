import static org.junit.Assert.*;

import org.junit.Test;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        // Test with a regular ClassLoader parent
        ClassLoader parent = new ClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        
        assertNotNull(xbusClassLoader);
        
        // Test with a null parent
        parent = null;
        xbusClassLoader = XBUSClassLoader.getInstance(parent); 
        
        assertNotNull(xbusClassLoader);
        
        // Test with a custom parent ClassLoader
        parent = new CustomClassLoader();
        xbusClassLoader = XBUSClassLoader.getInstance(parent); 
        
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        
        // Test with a directory containing no files
        String dirName = "test/resource/empty";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals(0, urls.size());
        
        // Test with a directory containing one non-jar/zip file
        dirName = "test/resource/nonjar";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals(0, urls.size());
        
        // Test with a directory containing one jar file
        dirName = "test/resource/jar1";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals(1, urls.size());
        
        // Test with a directory containing one zip file
        dirName = "test/resource/zip1";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals(1, urls.size());
        
        // Test with a directory containing both jar and zip files
        dirName = "test/resource";
        XBUSClassLoader.addUrls(urls, dirName);
        
        assertEquals(3, urls.size());
    }
}