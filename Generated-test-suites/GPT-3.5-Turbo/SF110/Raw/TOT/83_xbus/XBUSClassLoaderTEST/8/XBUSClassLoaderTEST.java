import org.junit.*;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(ClassLoader.getSystemClassLoader());
        assertNotNull(instance);
    }
    
    @Test
    public void testCreateClassLoader_withoutXBUS_HOME() {
        try {
            XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // expected exception
        }
    }
    
    @Test
    public void testCreateClassLoader_withXBUS_HOME() {
        // Simulate setting XBUS_HOME to a valid value
        Constants.XBUS_HOME = "C:/xbus";
        
        XBUSClassLoader instance = XBUSClassLoader.createClassLoader(ClassLoader.getSystemClassLoader());
        assertNotNull(instance);
    }
    
    @Test
    public void testAddUrls_withInvalidDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "invalid/dir");
        
        assertEquals(0, urls.size());
    }
    
    @Test
    public void testAddUrls_withValidDirName() {
        Vector urls = new Vector();
        XBUSClassLoader.addUrls(urls, "C:/xbus/lib");
        
        assertEquals(2, urls.size());
    }
}