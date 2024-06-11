import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testClassLoaderCreation() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
    }
    
    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "path/to/directory";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
    }
}